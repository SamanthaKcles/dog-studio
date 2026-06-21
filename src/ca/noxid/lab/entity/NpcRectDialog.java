package ca.noxid.lab.entity;

import ca.noxid.lab.EditorApp;
import ca.noxid.lab.gameinfo.GameInfo;
import ca.noxid.lab.rsrc.ResourceManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

// based on the bat i made
public class NpcRectDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final int SCALE_MIN = 1;
    private static final int SCALE_MAX = 8;
    private int scale = 2;
    private int entityResolution = 16;
    private int gridW = 16;
    private int gridH = 16;
    private static final int DEFAULT_VIEW_W = 332;
    private static final int DEFAULT_VIEW_H = 256;
    private GameInfo gameInfo;
    private final EditorApp parentApp;
    private BufferedImage currentImage;
    private Rectangle selectedRect;
    private final JComboBox<String> sourceCombo;
    private final JComboBox<String> imageCombo;
    private final ImagePanel        imagePanel;
    private final JScrollPane       scroll;
    private final JLabel            rectLabel;
    private JCheckBox               snapCheck;
    private final JSpinner     frameSpinner;
    private final JRadioButton radioH;
    private final JRadioButton radioV;
    private final JRadioButton radioLua;
    private final JRadioButton radioCpp;
    private final JRadioButton radioRust;
    private final JRadioButton radioNpcTbl;
    private final JTextArea    outputArea;
    private boolean populating = false;
    private JTextField gridWField;
    private JTextField gridHField;

    public NpcRectDialog(Frame owner) {
        super(owner, "Create Rects", false);
        this.parentApp = (EditorApp) owner;

        if (ResourceManager.cursor != null) setCursor(ResourceManager.cursor);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        sourceCombo = new JComboBox<>(new String[]{"NPC", "STAGE", "DATA"});
        imageCombo  = new JComboBox<>();
        sourceCombo.addActionListener(e -> { if (!populating) refreshImageList(); });
        imageCombo .addActionListener(e -> { if (!populating) loadSelectedImage(); });

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        topBar.add(new JLabel("Loads:"));
        topBar.add(sourceCombo);
        topBar.add(new JLabel("Image:"));
        topBar.add(imageCombo);
        JCheckBox snapCheck = new JCheckBox("Snap to grid");
        this.snapCheck = snapCheck;

        gridWField = new JTextField("16", 3);
        gridHField = new JTextField("16", 3);
        final Runnable applyGridSize = () -> {
            try {
                int div = (entityResolution == 32) ? 2 : 1;
                int w = Integer.parseInt(gridWField.getText().trim()) * div;
                int h = Integer.parseInt(gridHField.getText().trim()) * div;
                if (w > 0 && h > 0) {
                    gridW = w;
                    gridH = h;
                    repaint();
                }
            } catch (NumberFormatException ignored) {}
        };
        gridWField.addActionListener(e -> applyGridSize.run());
        gridHField.addActionListener(e -> applyGridSize.run());
        FocusAdapter gridFocus = new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) { applyGridSize.run(); }
        };
        gridWField.addFocusListener(gridFocus);
        gridHField.addFocusListener(gridFocus);

        topBar.add(snapCheck);
        topBar.add(new JLabel("Grid:"));
        topBar.add(gridWField);
        topBar.add(new JLabel("×"));
        topBar.add(gridHField);

        imagePanel = new ImagePanel();
        scroll = new JScrollPane(imagePanel);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(
                Math.min(DEFAULT_VIEW_W * scale + 4, 900),
                Math.min(DEFAULT_VIEW_H * scale + 4, 600)));
        scroll.setBorder(new TitledBorder("Sprite Sheet"));
        scroll.addMouseWheelListener(e -> {
            if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0)
                imagePanel.dispatchEvent(e);
        });

        rectLabel = new JLabel("No selection");
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        statusBar.add(rectLabel);

        JPanel leftCol = new JPanel(new BorderLayout(0, 0));
        leftCol.add(scroll,     BorderLayout.CENTER);
        leftCol.add(statusBar,  BorderLayout.SOUTH);

        frameSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        JComponent editor = frameSpinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setFont(
                    ((JSpinner.DefaultEditor) editor).getTextField().getFont().deriveFont(14f));
        }
        frameSpinner.setPreferredSize(new Dimension(
                frameSpinner.getPreferredSize().width, 30));

        radioH    = new JRadioButton("Horizontal", true);
        radioV    = new JRadioButton("Vertical",   false);
        radioLua    = new JRadioButton("Lua",     true);
        radioCpp    = new JRadioButton("C++",     false);
        radioRust   = new JRadioButton("Rust",    false);
        radioNpcTbl = new JRadioButton("npc.tbl", false);
        outputArea = new JTextArea();

        ButtonGroup dirGroup  = new ButtonGroup();
        dirGroup.add(radioH);
        dirGroup.add(radioV);

        ButtonGroup langGroup = new ButtonGroup();
        langGroup.add(radioLua);
        langGroup.add(radioCpp);
        langGroup.add(radioRust);
        langGroup.add(radioNpcTbl);

        frameSpinner.addChangeListener(e -> { updateDirectionState(); refreshOutput(); });
        radioH      .addActionListener(e -> refreshOutput());
        radioV      .addActionListener(e -> refreshOutput());
        radioLua    .addActionListener(e -> refreshOutput());
        radioCpp    .addActionListener(e -> refreshOutput());
        radioRust   .addActionListener(e -> refreshOutput());
        radioNpcTbl .addActionListener(e -> refreshOutput());

        JPanel rightCol = buildSidePanel();

        JPanel center = new JPanel(new BorderLayout(6, 0));
        center.add(leftCol,  BorderLayout.CENTER);
        center.add(rightCol, BorderLayout.EAST);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.add(topBar,  BorderLayout.NORTH);
        root.add(center,  BorderLayout.CENTER);

        setContentPane(root);
        pack();
        setMinimumSize(new Dimension(500, 340));
        setLocationRelativeTo(owner);

        updateDirectionState();
    }

    private JPanel buildSidePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Frame Generator"),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        panel.setPreferredSize(new Dimension(260, 0));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(2, 0, 2, 0);

        panel.add(new JLabel("Frames:"), c);
        c.gridy++;
        frameSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(frameSpinner, c);

        c.gridy++;
        c.insets = new Insets(8, 0, 2, 0);
        panel.add(new JLabel("Direction:"), c);
        c.insets = new Insets(0, 0, 0, 0);
        c.gridy++;
        panel.add(radioH, c);
        c.gridy++;
        panel.add(radioV, c);

        c.gridy++;
        c.insets = new Insets(8, 0, 2, 0);
        panel.add(new JLabel("Type:"), c);
        c.insets = new Insets(0, 0, 0, 0);
        c.gridy++;
        panel.add(radioLua, c);
        c.gridy++;
        panel.add(radioCpp, c);
        c.gridy++;
        panel.add(radioRust, c);
        c.gridy++;
        panel.add(radioNpcTbl, c);

        c.gridy++;
        c.insets = new Insets(8, 0, 2, 0);
        JButton copyBtn = new JButton("Copy");
        copyBtn.addActionListener(e -> copyOutput());
        panel.add(copyBtn, c);

        c.gridy++;
        c.insets = new Insets(2, 0, 0, 0);
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        outputArea.setEditable(false);
        outputArea.setLineWrap(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        JScrollPane outScroll = new JScrollPane(outputArea);
        outScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        outScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(outScroll, c);

        return panel;
    }

    private void updateDirectionState() {
        int frames = (Integer) frameSpinner.getValue();
        boolean multi = frames > 1;
        radioH.setEnabled(multi);
        radioV.setEnabled(multi);
    }

    private void refreshOutput() {
        if (selectedRect == null) {
            outputArea.setText("");
            return;
        }
        outputArea.setText(buildRectsText());
    }

    private void copyOutput() {
        String text = outputArea.getText();
        if (text == null || text.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "nothing to copy...",
                    "dude,", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Toolkit.getDefaultToolkit()
               .getSystemClipboard()
               .setContents(new StringSelection(text), null);
    }

    private String buildRectsText() {
        int left   = selectedRect.x;
        int top    = selectedRect.y;
        int width  = selectedRect.width;
        int height = selectedRect.height;
        int right  = left + width;
        int bottom = top  + height;
        int count  = (Integer) frameSpinner.getValue();
        boolean horiz = radioH.isSelected() || count == 1;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            int l, t, r, b;
            if (horiz) {
                l = left  + width  * i;  r = l + width;
                t = top;                  b = bottom;
            } else {
                l = left;                 r = right;
                t = top   + height * i;  b = t + height;
            }
            if (radioNpcTbl.isSelected()) {
                return String.format("DS|%d|%d|%d|%d", l, t, r, b);
            }
            if (i > 0) sb.append('\n');
            if (radioCpp.isSelected()) {
                sb.append(String.format("{%d, %d, %d, %d},", l, t, r, b));
            } else if (radioLua.isSelected()) {
                sb.append(String.format("ModCS.Rect.Create(%d, %d, %d, %d),", l, t, r, b));
            } else {
                sb.append(String.format("Rect { left: %d, top: %d, right: %d, bottom: %d },",
                        l, t, r, b));
            }
        }
        return sb.toString();
    }

    public void populate(GameInfo info) {
        this.gameInfo = info;
        this.entityResolution = info.getConfig().getEntityRes();
        selectedRect  = null;
        rectLabel.setText("No selection");
        gridW = entityResolution;
        gridH = entityResolution;
        gridWField.setText("16");
        gridHField.setText("16");
        populating = true;
        sourceCombo.setSelectedIndex(0);
        populating = false;
        refreshImageList();
    }

    private void refreshImageList() {
        if (gameInfo == null) return;
        String prev = (String) imageCombo.getSelectedItem();

        populating = true;
        imageCombo.removeAllItems();
        String source = (String) sourceCombo.getSelectedItem();

        if ("NPC".equals(source)) {
            for (String s : gameInfo.getNpcSheets()) imageCombo.addItem(s);
        } else if ("STAGE".equals(source)) {
            for (String s : gameInfo.getTilesets())  imageCombo.addItem(s);
        } else {
            String ext  = gameInfo.getImgExtension();
            File   dir  = gameInfo.getDataDirectory();
            File[] files = dir.listFiles((d, n) -> {
                String lo = n.toLowerCase();
                return lo.endsWith(".pbm") || lo.endsWith(".bmp") || lo.endsWith(".png");
            });
            if (files != null)
                for (File f : files) imageCombo.addItem(f.getName().replace(ext, ""));
        }
        populating = false;

        if (prev != null) {
            for (int i = 0; i < imageCombo.getItemCount(); i++) {
                if (prev.equals(imageCombo.getItemAt(i))) {
                    imageCombo.setSelectedIndex(i);
                    return;
                }
            }
        }
        if (imageCombo.getItemCount() > 0) {
            imageCombo.setSelectedIndex(0);
        } else {
            currentImage = null;
            imagePanel.revalidate();
            imagePanel.repaint();
        }
    }

    private void loadSelectedImage() {
        if (gameInfo == null) return;
        String sheet = (String) imageCombo.getSelectedItem();
        if (sheet == null) return;

        ResourceManager iMan    = parentApp.getImageManager();
        String          source  = (String) sourceCombo.getSelectedItem();
        String          ext     = gameInfo.getImgExtension();
        File            dataDir = gameInfo.getDataDirectory();

        File imgFile;
        if ("NPC".equals(source)) {
            imgFile = new File(dataDir, "Npc"   + File.separator + sheet + ext);
        } else if ("STAGE".equals(source)) {
            imgFile = new File(dataDir, "Stage" + File.separator + sheet + ext);
        } else {
            imgFile = new File(dataDir, sheet + ext);
        }

        if (imgFile.exists()) {
            iMan.addImage(imgFile, 1);
            currentImage = iMan.getImg(imgFile);
        } else {
            currentImage = null;
        }

        selectedRect = null;
        rectLabel.setText("No selection");
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    private class ImagePanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private int dragX0 = -1, dragY0 = -1;
        private int dragX1 = -1, dragY1 = -1;
        private boolean dragging = false;

        private int hoverRawX = -1, hoverRawY = -1;

        ImagePanel() {
            setBackground(Color.DARK_GRAY);

            MouseAdapter ma = new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    if (currentImage == null) return;
                    dragX0 = clampImgX(toImg(e.getX()));
                    dragY0 = clampImgY(toImg(e.getY()));
                    dragX1 = dragX0;
                    dragY1 = dragY0;
                    dragging = true;
                    repaint();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (!dragging || currentImage == null) return;
                    dragX1 = clampImgX(toImg(e.getX()));
                    dragY1 = clampImgY(toImg(e.getY()));
                    updateHover(e.getX(), e.getY());
                    updateSelection();
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (!dragging || currentImage == null) return;
                    dragging = false;
                    dragX1 = clampImgX(toImg(e.getX()));
                    dragY1 = clampImgY(toImg(e.getY()));
                    updateSelection();
                    repaint();
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    updateHover(e.getX(), e.getY());
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hoverRawX = hoverRawY = -1;
                    repaint();
                }

                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) {
                        int delta    = e.getWheelRotation();
                        int newScale = Math.max(SCALE_MIN, Math.min(SCALE_MAX, scale - delta));
                        if (newScale != scale) {
                            scale = newScale;
                            revalidate();
                            repaint();
                        }
                    } else {
                        getParent().dispatchEvent(e);
                    }
                }

                private void updateHover(int sx, int sy) {
                    if (currentImage == null) { hoverRawX = hoverRawY = -1; return; }
                    int div  = (entityResolution == 32) ? 2 : 1;
                    int ix = toImg(sx);
                    int iy = toImg(sy);
                    if (ix >= 0 && ix < currentImage.getWidth()  / div
                            && iy >= 0 && iy < currentImage.getHeight() / div) {
                        hoverRawX = ix;
                        hoverRawY = iy;
                    } else {
                        hoverRawX = hoverRawY = -1;
                    }
                }
            };
            addMouseListener(ma);
            addMouseMotionListener(ma);
            addMouseWheelListener(ma);
        }

        private int logicalStep()        { return (entityResolution == 32) ? 2 * dispScale() : dispScale(); }
        private int toImg(int screenPx)  { return screenPx / logicalStep(); }
        private int toDisp(int logPx)    { return logPx * logicalStep(); }
        private int snapGridX(int lx)   { return (lx / logicalGridW()) * logicalGridW(); }
        private int snapGridY(int ly)   { return (ly / logicalGridH()) * logicalGridH(); }
        private int logicalGridW()      { return gridW / ((entityResolution == 32) ? 2 : 1); }
        private int logicalGridH()      { return gridH / ((entityResolution == 32) ? 2 : 1); }

        private int dispScale() {
            return (entityResolution == 32) ? Math.max(1, scale / 2) : scale;
        }

        private int clampImgX(int lx) {
            int maxL = currentImage == null ? Integer.MAX_VALUE : currentImage.getWidth()  / ((entityResolution == 32) ? 2 : 1) - 1;
            return Math.max(0, Math.min(lx, maxL));
        }
        private int clampImgY(int ly) {
            int maxL = currentImage == null ? Integer.MAX_VALUE : currentImage.getHeight() / ((entityResolution == 32) ? 2 : 1) - 1;
            return Math.max(0, Math.min(ly, maxL));
        }

        private void updateSelection() {
            if (currentImage == null) return;
            int div  = (entityResolution == 32) ? 2 : 1;
            int imgLogW = currentImage.getWidth()  / div;
            int imgLogH = currentImage.getHeight() / div;
            int rx  = Math.min(dragX0, dragX1);
            int ry  = Math.min(dragY0, dragY1);
            int rx2 = Math.max(dragX0, dragX1) + 1;
            int ry2 = Math.max(dragY0, dragY1) + 1;

            if (snapCheck.isSelected()) {
                rx  = snapGridX(rx);
                ry  = snapGridY(ry);
                rx2 = snapGridX(rx2 + logicalGridW() - 1);
                ry2 = snapGridY(ry2 + logicalGridH() - 1);
            }

            rx2 = Math.min(rx2, imgLogW);
            ry2 = Math.min(ry2, imgLogH);
            selectedRect = new Rectangle(rx, ry, rx2 - rx, ry2 - ry);
            rectLabel.setText(String.format(
                    "Rect:  %d,  %d,  %d,  %d   (%d × %d px)",
                    rx, ry, rx2, ry2, rx2 - rx, ry2 - ry));
            refreshOutput();
        }

        @Override
        public Dimension getPreferredSize() {
            int ls = logicalStep();
            int div = (entityResolution == 32) ? 2 : 1;
            int logW = currentImage != null ? currentImage.getWidth()  / div : DEFAULT_VIEW_W;
            int logH = currentImage != null ? currentImage.getHeight() / div : DEFAULT_VIEW_H;
            return new Dimension(logW * ls, logH * ls);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            int imgW  = currentImage != null ? currentImage.getWidth()  : DEFAULT_VIEW_W;
            int imgH  = currentImage != null ? currentImage.getHeight() : DEFAULT_VIEW_H;
            int ds    = dispScale();
            int dispW = imgW * ds;
            int dispH = imgH * ds;

            if (currentImage != null) {
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g2.drawImage(currentImage, 0, 0, dispW, dispH, null);
            }

            if (ds >= 2 || entityResolution == 32) drawSubGrid(g2, dispW, dispH);
            drawTileGrid(g2, dispW, dispH);

            if (selectedRect != null) {
                drawSelRect(g2,
                        toDisp(selectedRect.x),
                        toDisp(selectedRect.y),
                        toDisp(selectedRect.width),
                        toDisp(selectedRect.height));
            }

            if (!dragging && hoverRawX >= 0) {
                int ls = logicalStep();
                g2.setPaintMode();
                g2.setStroke(new BasicStroke(3f));
                g2.setColor(Color.BLACK);
                g2.drawRect(toDisp(hoverRawX), toDisp(hoverRawY), ls, ls);
                g2.setStroke(new BasicStroke(1f));
                g2.setColor(Color.WHITE);
                g2.drawRect(toDisp(hoverRawX), toDisp(hoverRawY), ls, ls);
            }

            g2.dispose();
        }

        private void drawSubGrid(Graphics2D g2, int dispW, int dispH) {
            int ls = logicalStep();
            if (ls < 2) return;
            g2.setPaintMode();
            g2.setColor(new Color(255, 255, 255, 20));
            g2.setStroke(new BasicStroke(1f));
            int lgW = logicalGridW();
            int lgH = logicalGridH();
            for (int dx = ls; dx < dispW; dx += ls) {
                int logX = dx / ls;
                if (logX % lgW == 0) continue;
                g2.drawLine(dx, 0, dx, dispH);
            }
            for (int dy = ls; dy < dispH; dy += ls) {
                int logY = dy / ls;
                if (logY % lgH == 0) continue;
                g2.drawLine(0, dy, dispW, dy);
            }
        }

        private void drawTileGrid(Graphics2D g2, int dispW, int dispH) {
            g2.setPaintMode();
            g2.setStroke(new BasicStroke(1f));
            int ls    = logicalStep();
            int cellX = logicalGridW() * ls;
            int cellY = logicalGridH() * ls;
            for (int dx = 0; dx <= dispW; dx += cellX) {
                g2.setColor(((dx / cellX) % 8 == 0)
                        ? new Color(255, 255, 255, 120)
                        : new Color(255, 255, 255, 70));
                g2.drawLine(dx, 0, dx, dispH);
            }
            for (int dy = 0; dy <= dispH; dy += cellY) {
                g2.setColor(((dy / cellY) % 8 == 0)
                        ? new Color(255, 255, 255, 120)
                        : new Color(255, 255, 255, 70));
                g2.drawLine(0, dy, dispW, dy);
            }
        }

        private void drawSelRect(Graphics2D g2, int x, int y, int w, int h) {
            g2.setPaintMode();
            g2.setStroke(new BasicStroke(3f));
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, w, h);
            g2.setStroke(new BasicStroke(1f));
            g2.setColor(Color.WHITE);
            g2.drawRect(x, y, w, h);
        }
    }
} //              what if...         poopy butt
