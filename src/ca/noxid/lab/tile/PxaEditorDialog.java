package ca.noxid.lab.tile;

import ca.noxid.lab.Messages;
import ca.noxid.lab.mapdata.MapInfo;
import ca.noxid.lab.rsrc.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;

public class PxaEditorDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final String[] tileNames = {
            Messages.getString("TilesetPane.0"),  Messages.getString("TilesetPane.0"),
            Messages.getString("TilesetPane.2"),  Messages.getString("TilesetPane.3"),
            Messages.getString("TilesetPane.3"),  Messages.getString("TilesetPane.5"),
            Messages.getString("TilesetPane.0"),  Messages.getString("TilesetPane.0"),
            Messages.getString("TilesetPane.0"),  Messages.getString("TilesetPane.0"),
            Messages.getString("TilesetPane.0"),  Messages.getString("TilesetPane.0"),
            Messages.getString("TilesetPane.0"),  Messages.getString("TilesetPane.0"),
            Messages.getString("TilesetPane.0"),  Messages.getString("TilesetPane.0"),
            Messages.getString("TilesetPane.0"),  Messages.getString("TilesetPane.0"),
            Messages.getString("TilesetPane.0"),  Messages.getString("TilesetPane.0"),
            Messages.getString("TilesetPane.0"),  Messages.getString("TilesetPane.0"),
            Messages.getString("TilesetPane.0"),  Messages.getString("TilesetPane.0"),
            Messages.getString("TilesetPane.0"),  Messages.getString("TilesetPane.0"),
            Messages.getString("TilesetPane.0"),  Messages.getString("TilesetPane.0"),
            Messages.getString("TilesetPane.0"),  Messages.getString("TilesetPane.0"),
            Messages.getString("TilesetPane.0"),  Messages.getString("TilesetPane.0"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.33"), Messages.getString("TilesetPane.33"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.65"),
            Messages.getString("TilesetPane.66"), Messages.getString("TilesetPane.67"),
            Messages.getString("TilesetPane.68"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.70"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.80"), Messages.getString("TilesetPane.81"),
            Messages.getString("TilesetPane.82"), Messages.getString("TilesetPane.83"),
            Messages.getString("TilesetPane.84"), Messages.getString("TilesetPane.85"),
            Messages.getString("TilesetPane.86"), Messages.getString("TilesetPane.87"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.96"), Messages.getString("TilesetPane.97"),
            Messages.getString("TilesetPane.98"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.100"),Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.112"),Messages.getString("TilesetPane.113"),
            Messages.getString("TilesetPane.114"),Messages.getString("TilesetPane.115"),
            Messages.getString("TilesetPane.116"),Messages.getString("TilesetPane.117"),
            Messages.getString("TilesetPane.118"),Messages.getString("TilesetPane.119"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.128"),Messages.getString("TilesetPane.129"),
            Messages.getString("TilesetPane.130"),Messages.getString("TilesetPane.131"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.160"),Messages.getString("TilesetPane.161"),
            Messages.getString("TilesetPane.162"),Messages.getString("TilesetPane.163"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
            Messages.getString("TilesetPane.77"), Messages.getString("TilesetPane.77"),
    };
    private final ResourceManager iMan;
    private final MapInfo dataHolder;
    private final byte[] workingPxa;
    private int selTypeX0 = 0, selTypeY0 = 0;
    private int selTypeX1 = 0, selTypeY1 = 0;
    private int selectedType = 0;
    private final Deque<byte[]> undoStack = new ArrayDeque<>();
    private final Deque<byte[]> redoStack = new ArrayDeque<>();
    private static final int MAX_UNDO = 100;
    private TilesetEditPanel tilesetPanel;
    private TypePickerPanel  typePickerPanel;
    private JLabel           statusLabel;
    private int hoveredTile = -1;

    public PxaEditorDialog(Frame owner, MapInfo data, ResourceManager imageManager) {
        super(owner, "Edit .pxa – " + data.getPxa().getName(), true);
        this.dataHolder = data;
        this.iMan       = imageManager;

        byte[] real = iMan.getPxa(dataHolder.getPxa());
        this.workingPxa = new byte[real.length];
        System.arraycopy(real, 0, this.workingPxa, 0, real.length);

        if (ResourceManager.cursor != null) setCursor(ResourceManager.cursor);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { doCancel(); }
        });

        buildUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(4, 4));
        root.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.5);
        split.setOneTouchExpandable(false);

        int panelPx = TypePickerPanel.CELL_DISP * 16;

        tilesetPanel = new TilesetEditPanel();
        JScrollPane tileScroll = new JScrollPane(tilesetPanel);
        tileScroll.setBorder(BorderFactory.createTitledBorder("Tileset"));
        tileScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tileScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        tileScroll.setPreferredSize(new Dimension(panelPx + 20, panelPx + 40));
        split.setLeftComponent(tileScroll);

        typePickerPanel = new TypePickerPanel();
        JPanel typeWrap = new JPanel(new BorderLayout());
        typeWrap.setBorder(BorderFactory.createTitledBorder("Tile type"));
        typeWrap.add(typePickerPanel, BorderLayout.CENTER);
        typeWrap.setPreferredSize(new Dimension(panelPx + 20, panelPx + 40));
        split.setRightComponent(typeWrap);

        root.add(split, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(4, 0));
        statusLabel = new JLabel("Tile: –");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        bottomPanel.add(statusLabel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        JButton saveBtn   = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        saveBtn.addActionListener(e -> doSave());
        cancelBtn.addActionListener(e -> doCancel());
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        bottomPanel.add(btnPanel, BorderLayout.EAST);

        root.add(bottomPanel, BorderLayout.SOUTH);
        setContentPane(root);

        KeyStroke undoKs  = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
        KeyStroke redoKs1 = KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK);
        KeyStroke redoKs2 = KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);

        JPanel glass = new JPanel();
        glass.setOpaque(false);
        glass.setLayout(null);
        setGlassPane(glass);
        glass.setVisible(true);

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(undoKs,  "pxa-undo");
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(redoKs1, "pxa-redo");
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(redoKs2, "pxa-redo");
        root.getActionMap().put("pxa-undo", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { doUndo(); }
        });
        root.getActionMap().put("pxa-redo", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { doRedo(); }
        });
    }

    private void pushUndo() {
        byte[] snap = new byte[workingPxa.length];
        System.arraycopy(workingPxa, 0, snap, 0, workingPxa.length);
        undoStack.push(snap);
        if (undoStack.size() > MAX_UNDO) {
            byte[][] arr = undoStack.toArray(new byte[0][]);
            undoStack.clear();
            for (int i = 0; i < arr.length - 1; i++) undoStack.add(arr[i]);
        }
        redoStack.clear();
        updateStatus();
    }

    private void doUndo() {
        if (undoStack.isEmpty()) return;
        byte[] snap = new byte[workingPxa.length];
        System.arraycopy(workingPxa, 0, snap, 0, workingPxa.length);
        redoStack.push(snap);
        byte[] prev = undoStack.pop();
        System.arraycopy(prev, 0, workingPxa, 0, workingPxa.length);
        tilesetPanel.repaint();
        updateStatus();
    }

    private void doRedo() {
        if (redoStack.isEmpty()) return;
        byte[] snap = new byte[workingPxa.length];
        System.arraycopy(workingPxa, 0, snap, 0, workingPxa.length);
        undoStack.push(snap);
        byte[] next = redoStack.pop();
        System.arraycopy(next, 0, workingPxa, 0, workingPxa.length);
        tilesetPanel.repaint();
        updateStatus();
    }

    private void doSave() {
        byte[] real = iMan.getPxa(dataHolder.getPxa());
        System.arraycopy(workingPxa, 0, real, 0, workingPxa.length);
        iMan.savePxa(dataHolder.getPxa());
        dataHolder.markChanged();
        setVisible(false);
    }

    private void doCancel() {
        setVisible(false);
    }

    private int getTileType(int index) {
        if (index < 0 || index >= workingPxa.length) return 0;
        return workingPxa[index] & 0xFF;
    }

    private void setTileType(int index, int type) {
        if (index < 0 || index >= workingPxa.length) return;
        workingPxa[index] = (byte) type;
    }

    private String safeTypeName(int type) {
        if (type >= 0 && type < tileNames.length) return tileNames[type];
        return "";
    }

    private int tilesWide() {
        BufferedImage img = iMan.getImg(dataHolder.getTileset());
        return img == null ? 16 : img.getWidth() / dataHolder.getConfig().getTileSize();
    }

    private int tilesHigh() {
        BufferedImage img = iMan.getImg(dataHolder.getTileset());
        return img == null ? 16 : img.getHeight() / dataHolder.getConfig().getTileSize();
    }

    private void updateStatus() {
        if (statusLabel == null) return;
        if (hoveredTile >= 0) {
            statusLabel.setText("Tile: #" + hoveredTile);
        } else {
            statusLabel.setText("Tile: –");
        }
    }

    private class TilesetEditPanel extends JPanel {

        private static final long serialVersionUID = 1L;
        static final int SC = 32;
        private int dragX0 = -1, dragY0 = -1;
        private int dragX1 = -1, dragY1 = -1;
        private boolean active = false;
        private int cursorTX = -1, cursorTY = -1;

        TilesetEditPanel() {
            MouseAdapter ma = new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    updateCursor(e);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    updateCursor(e);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    cursorTX = cursorTY = -1;
                    hoveredTile = -1;
                    updateStatus();
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    int tx = clamp(e.getX() / SC, 0, tilesWide() - 1);
                    int ty = clamp(e.getY() / SC, 0, tilesHigh() - 1);
                    dragX0 = tx; dragY0 = ty;
                    dragX1 = tx; dragY1 = ty;
                    active = true;
                    repaint();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (!active) return;
                    dragX1 = clamp(e.getX() / SC, 0, tilesWide() - 1);
                    dragY1 = clamp(e.getY() / SC, 0, tilesHigh() - 1);
                    updateCursor(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (!active) return;
                    dragX1 = clamp(e.getX() / SC, 0, tilesWide() - 1);
                    dragY1 = clamp(e.getY() / SC, 0, tilesHigh() - 1);
                    active = false;

                    pushUndo();

                    int brushW = selTypeX1 - selTypeX0 + 1;
                    int brushH = selTypeY1 - selTypeY0 + 1;
                    int rawX0 = Math.min(dragX0, dragX1);
                    int rawY0 = Math.min(dragY0, dragY1);
                    int rawX1 = Math.max(dragX0, dragX1);
                    int rawY1 = Math.max(dragY0, dragY1);
                    int x0 = rawX0;
                    int y0 = rawY0;
                    int x1 = Math.min(rawX1 + brushW - 1, tilesWide() - 1);
                    int y1 = Math.min(rawY1 + brushH - 1, tilesHigh() - 1);
                    int wide = tilesWide();

                    for (int ty = y0; ty <= y1; ty++) {
                        for (int tx = x0; tx <= x1; tx++) {
                            int bx = (tx - x0) % brushW + selTypeX0;
                            int by = (ty - y0) % brushH + selTypeY0;
                            setTileType(ty * wide + tx, by * 0x10 + bx);
                        }
                    }

                    dragX0 = dragY0 = dragX1 = dragY1 = -1;
                    repaint();
                }

                private void updateCursor(MouseEvent e) {
                    int tx = e.getX() / SC;
                    int ty = e.getY() / SC;
                    if (tx >= 0 && tx < tilesWide() && ty >= 0 && ty < tilesHigh()) {
                        cursorTX = tx;
                        cursorTY = ty;
                        hoveredTile = ty * tilesWide() + tx;
                    } else {
                        cursorTX = cursorTY = -1;
                        hoveredTile = -1;
                    }
                    updateStatus();
                    repaint();
                }
            };
            addMouseListener(ma);
            addMouseMotionListener(ma);
        }

        private int clamp(int v, int lo, int hi) { return Math.max(lo, Math.min(hi, v)); }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(tilesWide() * SC, tilesHigh() * SC);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();

            int wide = tilesWide();
            int high = tilesHigh();
            BufferedImage tileImg = iMan.getImg(dataHolder.getTileset());
            BufferedImage legend  = iMan.getImg(ResourceManager.rsrcTiles);

            for (int ty = 0; ty < high; ty++) {
                for (int tx = 0; tx < wide; tx++) {
                    g2.setColor((tx + ty) % 2 == 0
                            ? TilesetPane.bgCol.brighter() : TilesetPane.bgCol.darker());
                    g2.fillRect(tx * SC, ty * SC, SC, SC);
                }
            }

            if (tileImg != null)
                g2.drawImage(tileImg, 0, 0, wide * SC, high * SC,
                        0, 0, tileImg.getWidth(), tileImg.getHeight(), this);

            if (legend != null) {
                for (int ty = 0; ty < high; ty++) {
                    for (int tx = 0; tx < wide; tx++) {
                        int type = getTileType(ty * wide + tx);
                        int srcX = (type % 0x10) * 16;
                        int srcY = (type / 0x10) * 16;
                        g2.drawImage(legend,
                                tx * SC, ty * SC, tx * SC + SC, ty * SC + SC,
                                srcX, srcY, srcX + 16, srcY + 16, this);
                    }
                }
            }

            g2.setStroke(new BasicStroke(2f));
            g2.setXORMode(Color.WHITE);

            if (active && dragX0 >= 0) {
                int brushW = selTypeX1 - selTypeX0 + 1;
                int brushH = selTypeY1 - selTypeY0 + 1;
                int rawX0 = Math.min(dragX0, dragX1);
                int rawY0 = Math.min(dragY0, dragY1);
                int rawX1 = Math.max(dragX0, dragX1);
                int rawY1 = Math.max(dragY0, dragY1);
                int rx0 = rawX0;
                int ry0 = rawY0;
                int rx1 = Math.min(rawX1 + brushW - 1, wide - 1);
                int ry1 = Math.min(rawY1 + brushH - 1, high - 1);
                g2.drawRoundRect(rx0 * SC, ry0 * SC, (rx1 - rx0 + 1) * SC, (ry1 - ry0 + 1) * SC, 8, 8);
            }

            if (!active && cursorTX >= 0) {
                int brushW = selTypeX1 - selTypeX0 + 1;
                int brushH = selTypeY1 - selTypeY0 + 1;
                int bx = Math.min(cursorTX, wide - brushW);
                int by = Math.min(cursorTY, high - brushH);
                g2.drawRoundRect(bx * SC, by * SC, brushW * SC, brushH * SC, 8, 8);
            }

            g2.dispose();
        }
    }

    private class TypePickerPanel extends JPanel {

        private static final long serialVersionUID = 1L;
        private static final int CELL = 16;
        static final int CELL_DISP = CELL * 2;
        private int anchorCol = 0, anchorRow = 0;
        private int liveCol = 0, liveRow = 0;
        private boolean dragging = false;

        TypePickerPanel() {
            MouseAdapter ma = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int tx = clamp(e.getX() / CELL_DISP, 0, 15);
                    int ty = clamp(e.getY() / CELL_DISP, 0, 15);
                    anchorCol = tx; anchorRow = ty;
                    liveCol   = tx; liveRow   = ty;
                    dragging = true;
                    repaint();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    liveCol = clamp(e.getX() / CELL_DISP, 0, 15);
                    liveRow = clamp(e.getY() / CELL_DISP, 0, 15);
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    liveCol = clamp(e.getX() / CELL_DISP, 0, 15);
                    liveRow = clamp(e.getY() / CELL_DISP, 0, 15);
                    dragging = false;

                    selTypeX0 = Math.min(anchorCol, liveCol);
                    selTypeY0 = Math.min(anchorRow, liveRow);
                    selTypeX1 = Math.max(anchorCol, liveCol);
                    selTypeY1 = Math.max(anchorRow, liveRow);
                    selectedType = selTypeY0 * 0x10 + selTypeX0;

                    updateStatus();
                    repaint();
                    tilesetPanel.repaint();
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    int tx = e.getX() / CELL_DISP;
                    int ty = e.getY() / CELL_DISP;
                    if (tx >= 0 && tx < 16 && ty >= 0 && ty < 16)
                        setToolTipText(String.format("0x%02X – %s",
                                ty * 0x10 + tx, safeTypeName(ty * 0x10 + tx)));
                }

                private int clamp(int v, int lo, int hi) { return Math.max(lo, Math.min(hi, v)); }
            };
            addMouseListener(ma);
            addMouseMotionListener(ma);
            setToolTipText("");
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(16 * CELL_DISP, 16 * CELL_DISP);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());

            BufferedImage legend = iMan.getImg(ResourceManager.rsrcTiles);
            if (legend != null) {
                int d = 16 * CELL_DISP;
                g2.drawImage(legend, 0, 0, d, d, 0, 0, legend.getWidth(), legend.getHeight(), this);
                g2.drawImage(legend, 0, 0, d, d, 0, 0, legend.getWidth(), legend.getHeight(), this);
            }

            g2.setStroke(new BasicStroke(2f));
            g2.setXORMode(Color.WHITE);

            int cx0 = selTypeX0, cy0 = selTypeY0;
            int cw  = (selTypeX1 - selTypeX0 + 1) * CELL_DISP;
            int ch  = (selTypeY1 - selTypeY0 + 1) * CELL_DISP;
            g2.drawRoundRect(cx0 * CELL_DISP, cy0 * CELL_DISP, cw, ch, 4, 4);

            if (dragging) {
                int dx0 = Math.min(anchorCol, liveCol);
                int dy0 = Math.min(anchorRow, liveRow);
                int dw  = (Math.abs(liveCol - anchorCol) + 1) * CELL_DISP;
                int dh  = (Math.abs(liveRow - anchorRow) + 1) * CELL_DISP;
                g2.drawRoundRect(dx0 * CELL_DISP, dy0 * CELL_DISP, dw, dh, 4, 4);
            }

            g2.dispose();
        }
    }
}
