package ca.noxid.lab;

import ca.noxid.lab.rsrc.ResourceManager;
import com.carrotlord.string.StrTools;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ThemeDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private static final int PREVIEW_W = 427;
	private static final int PREVIEW_H = 240;
	private static final int CARD_BORDER = 1;
	private static final int CARD_PADDING = 6;
	private static final int CARD_W = PREVIEW_W + CARD_BORDER * 2 + CARD_PADDING * 2;
	private static final int CARD_H = PREVIEW_H + CARD_BORDER * 2 + 100;
	private static final Color COLOR_SELECTED = new Color(75, 110, 175);
	private static final Color COLOR_HOVER    = new Color(100, 130, 190);
	private static final Color COLOR_NORMAL   = UIManager.getColor("Panel.background") != null
			? UIManager.getColor("Panel.background") : new Color(60, 63, 65);

	private static class ThemeInfo {
		String id, name, author, description;
		boolean lightUI = false;
		Color panelColor      = Color.decode("#2b2b2b");
		Color splashColor     = Color.decode("#000001");
		Color bg              = Color.decode("#2b2b2b");
		Color bgLighter       = Color.decode("#3c3f41");
		Color bgField         = Color.decode("#454a4a");
		Color fg              = Color.decode("#bbbbbb");
		Color fgBright        = Color.decode("#dcdcdc");
		Color selection       = Color.decode("#4b6eaf");
		Color border          = Color.decode("#505050");
		Color buttonBg        = Color.decode("#4d5052");
		Color inactiveFg      = Color.decode("#967878");
		Color mapOutline      = Color.decode("#727983");
		Color tscTagFg        = Color.decode("#ff96d7");
		Color tscTagBg        = Color.decode("#3c3c50");
		Color scrollBar       = null; //buttonBg
		Color scrollBg        = null; //bg
		Color scriptBg        = null; //bgField
		Color panelOutline    = null; //border
		Color dividerCol      = null; //bgLighter
		ThemeInfo(String id) {
			this.id = id; this.name = id; this.author = ""; this.description = "";
		}
	}

	private static List<ThemeInfo> cachedThemeInfo = null;

	public static boolean isCurrentThemeLight() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) {
			if (t.id.equals(current)) {
				return t.lightUI;
			}
		}
		return false;
	}

	public static Color getCurrentPanelColor() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) {
			if (t.id.equals(current)) return t.panelColor;
		}
		return Color.decode("#2b2b2b");
	}

	public static Color getCurrentSplashColor() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) {
			if (t.id.equals(current)) return t.splashColor;
		}
		return Color.decode("#000001");
	}

	public static Color getCurrentBg() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) { if (t.id.equals(current)) return t.bg; }
		return Color.decode("#2b2b2b");
	}

	public static Color getCurrentBgLighter() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) { if (t.id.equals(current)) return t.bgLighter; }
		return Color.decode("#3c3f41");
	}

	public static Color getCurrentBgField() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) { if (t.id.equals(current)) return t.bgField; }
		return Color.decode("#454a4a");
	}

	public static Color getCurrentFg() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) { if (t.id.equals(current)) return t.fg; }
		return Color.decode("#bbbbbb");
	}

	public static Color getCurrentFgBright() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) { if (t.id.equals(current)) return t.fgBright; }
		return Color.decode("#dcdcdc");
	}

	public static Color getCurrentSelection() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) { if (t.id.equals(current)) return t.selection; }
		return Color.decode("#4b6eaf");
	}

	public static Color getCurrentBorder() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) { if (t.id.equals(current)) return t.border; }
		return Color.decode("#505050");
	}

	public static Color getCurrentButtonBg() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) { if (t.id.equals(current)) return t.buttonBg; }
		return Color.decode("#4d5052");
	}

	public static Color getCurrentInactiveFg() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) { if (t.id.equals(current)) return t.inactiveFg; }
		return Color.decode("#967878");
	}

	public static Color getCurrentMapOutline() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) { if (t.id.equals(current)) return t.mapOutline; }
		return Color.decode("#727983");
	}

	public static Color getCurrentTscTagFg() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) { if (t.id.equals(current)) return t.tscTagFg; }
		return Color.decode("#ff96d7");
	}

	public static Color getCurrentTscTagBg() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) { if (t.id.equals(current)) return t.tscTagBg; }
		return Color.decode("#3c3c50");
	}

	public static Color getCurrentScrollBar() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) {
			if (t.id.equals(current)) return t.scrollBar != null ? t.scrollBar : t.buttonBg;
		}
		return Color.decode("#4d5052");
	}

	public static Color getCurrentScrollBg() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) {
			if (t.id.equals(current)) return t.scrollBg != null ? t.scrollBg : t.bg;
		}
		return Color.decode("#2b2b2b");
	}

	public static Color getCurrentScriptBg() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) {
			if (t.id.equals(current)) return t.scriptBg != null ? t.scriptBg : t.bgField;
		}
		return Color.decode("#454a4a");
	}

	public static Color getCurrentPanelOutline() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) {
			if (t.id.equals(current)) return t.panelOutline != null ? t.panelOutline : t.border;
		}
		return Color.decode("#505050");
	}

	public static Color getCurrentDividerCol() {
		String current = EditorApp.getTheme();
		for (ThemeInfo t : getThemeInfo()) {
			if (t.id.equals(current)) return t.dividerCol != null ? t.dividerCol : t.bgLighter;
		}
		return Color.decode("#3c3f41");
	}

	static List<ThemeInfo> getThemeInfo() {
		if (cachedThemeInfo == null) {
			cachedThemeInfo = loadThemeInfo();
		}
		return cachedThemeInfo;
	}

	private class ThemeCard extends JPanel {
		private static final long serialVersionUID = 1L;
		final ThemeInfo info;
		private boolean selected = false;

		ThemeCard(ThemeInfo info) {
			this.info = info;
			setLayout(new BorderLayout(0, 4));
			setBorder(new CompoundBorder(
					new LineBorder(Color.GRAY, 1, true),
					new EmptyBorder(6, 6, 6, 6)));
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			JLabel imgLabel = new JLabel();
			imgLabel.setPreferredSize(new Dimension(PREVIEW_W, PREVIEW_H));
			imgLabel.setMinimumSize(new Dimension(PREVIEW_W, PREVIEW_H));
			imgLabel.setMaximumSize(new Dimension(PREVIEW_W, PREVIEW_H));
			imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
			imgLabel.setVerticalAlignment(SwingConstants.CENTER);
			imgLabel.setOpaque(true);
			imgLabel.setBackground(new Color(30, 30, 30));

			Image previewImg = null;
			File extDir = ResourceManager.getExternalThemeDir(info.id);
			if (extDir != null) {
				File extPreview = new File(extDir, "preview.png");
				if (extPreview.exists()) {
					try { previewImg = ImageIO.read(extPreview); } catch (IOException ignored) {}
				}
			}
			if (previewImg == null) {
				URL imgUrl = EditorApp.class.getResource("rsrc/" + info.id + "/preview.png");
				if (imgUrl != null) {
					previewImg = new ImageIcon(imgUrl).getImage();
				}
			}
			if (previewImg != null) {
				Image scaled = previewImg.getScaledInstance(PREVIEW_W, PREVIEW_H, Image.SCALE_SMOOTH);
				imgLabel.setIcon(new ImageIcon(scaled));
			} else {
				imgLabel.setText("No preview");
				imgLabel.setForeground(Color.GRAY);
			}

			JLabel nameLabel = new JLabel(info.name.isEmpty() ? info.id : info.name);
			nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 16f));

			JPanel textPanel = new JPanel();
			textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
			textPanel.setOpaque(false);
			textPanel.add(nameLabel);

			if (!info.author.isEmpty()) {
				JLabel authorLabel = new JLabel("by " + info.author);
				authorLabel.setFont(authorLabel.getFont().deriveFont(Font.PLAIN, 13f));
				authorLabel.setForeground(UIManager.getColor("Label.foreground") != null
						? UIManager.getColor("Label.foreground").darker()
						: Color.GRAY);
				textPanel.add(Box.createVerticalStrut(3));
				textPanel.add(authorLabel);
			}

			if (!info.description.isEmpty()) {
				JLabel descLabel = new JLabel(
						"<html><body style='width:" + PREVIEW_W + "px'>" + info.description + "</body></html>");
				descLabel.setFont(descLabel.getFont().deriveFont(Font.PLAIN, 13f));
				textPanel.add(Box.createVerticalStrut(6));
				textPanel.add(descLabel);
			}

			add(imgLabel, BorderLayout.NORTH);
			add(textPanel, BorderLayout.CENTER);

			addMouseListener(new MouseAdapter() {
				@Override public void mouseClicked(MouseEvent e) { selectCard(ThemeCard.this); }
				@Override public void mouseEntered(MouseEvent e) { if (!selected) highlight(true); }
				@Override public void mouseExited(MouseEvent e)  { if (!selected) highlight(false); }
			});
		}

		@Override public Dimension getPreferredSize() { return new Dimension(CARD_W, CARD_H); }
		@Override public Dimension getMinimumSize()   { return getPreferredSize(); }
		@Override public Dimension getMaximumSize()   { return getPreferredSize(); }

		void setSelected(boolean sel) {
			selected = sel;
			setBorder(new CompoundBorder(
					new LineBorder(sel ? COLOR_SELECTED : Color.GRAY, sel ? 2 : 1, true),
					new EmptyBorder(sel ? 5 : 6, sel ? 5 : 6, sel ? 5 : 6, sel ? 5 : 6)));
			repaint();
		}

		private void highlight(boolean on) {
			setBorder(new CompoundBorder(
					new LineBorder(on ? COLOR_HOVER : Color.GRAY, 1, true),
					new EmptyBorder(6, 6, 6, 6)));
			repaint();
		}
	}

	private final List<ThemeCard> cards = new ArrayList<>();
	private ThemeCard selectedCard = null;

	public ThemeDialog(EditorApp parent, ResourceManager iMan) {
		super(parent, "Select Theme", true);

		List<ThemeInfo> themes = getThemeInfo();

		final int COLS = 2;
		final int GAP = 12;
		JPanel cardsPanel = new JPanel(new GridLayout(0, COLS, GAP, GAP));
		cardsPanel.setBorder(new EmptyBorder(GAP, GAP, GAP, GAP));

		String currentTheme = EditorApp.getTheme();
		for (ThemeInfo info : themes) {
			ThemeCard card = new ThemeCard(info);
			cards.add(card);
			cardsPanel.add(card);
			if (info.id.equals(currentTheme)) {
				selectedCard = card;
			}
		}

		JScrollPane scroll = new JScrollPane(cardsPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setUnitIncrement(32);

		int sbW = scroll.getVerticalScrollBar().getPreferredSize().width;
		int panelW = CARD_W * COLS + GAP * (COLS - 1) + GAP * 2;
		int scrollW = panelW + sbW;
		int scrollH = CARD_H * 2 + GAP + GAP * 2 + 50;
		scroll.setPreferredSize(new Dimension(scrollW, scrollH));

		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		okButton.addActionListener(e -> applyTheme());
		cancelButton.addActionListener(e -> dispose());
		getRootPane().setDefaultButton(okButton);

		JButton getThemesButton = new JButton("Get Themes");
		getThemesButton.addActionListener(e -> {
			try {
				Desktop.getDesktop().browse(new URI("https://samandk.com/themes"));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this,
						"Could not open browser: " + ex.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
		leftPanel.add(getThemesButton);

		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
		rightPanel.add(okButton);
		rightPanel.add(cancelButton);

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(leftPanel, BorderLayout.WEST);
		buttonPanel.add(rightPanel, BorderLayout.EAST);

		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		if (selectedCard != null) {
			selectedCard.setSelected(true);
		} else if (!cards.isEmpty()) {
			selectCard(cards.get(0));
		}

		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
		setVisible(true);
	}

	private void selectCard(ThemeCard card) {
		if (selectedCard != null) selectedCard.setSelected(false);
		selectedCard = card;
		card.setSelected(true);
	}

	private void applyTheme() {
		if (selectedCard == null) { dispose(); return; }
		String selectedId = selectedCard.info.id;
		String currentId  = EditorApp.getTheme();
		if (!selectedId.equals(currentId)) {
			EditorApp.setTheme(selectedId);
			int response = JOptionPane.showConfirmDialog(
					this,
					"Theme changed to '" + selectedCard.info.name + "'.\nRestart now to apply?",
					"Restart required",
					JOptionPane.YES_NO_OPTION);
			dispose();
			if (response == JOptionPane.YES_OPTION) {
				EditorApp.restartApplication();
			}
		} else {
			dispose();
		}
	}

	static List<ThemeInfo> loadThemeInfo() {
		List<ThemeInfo> result = new ArrayList<>();

		File themesDir = ResourceManager.getExternalThemesDir();
		if (themesDir != null && themesDir.isDirectory()) {
			List<String> orderedIds = new ArrayList<>();
			File themeIdFile = ResourceManager.getExternalThemeId();
			if (themeIdFile != null && themeIdFile.exists()) {
				try (BufferedReader br = new BufferedReader(
						new InputStreamReader(new FileInputStream(themeIdFile), "UTF-8"))) {
					String line;
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (!line.isEmpty() && !line.startsWith("#")) orderedIds.add(line);
					}
				} catch (IOException e) {
					System.out.println("Could not read " + themeIdFile + ": " + e.getMessage());
				}
			}

			File[] subdirs = themesDir.listFiles(File::isDirectory);
			if (subdirs != null) {
				for (File dir : subdirs) {
					if (!orderedIds.contains(dir.getName())) orderedIds.add(dir.getName());
				}
			}

			for (String id : orderedIds) {
				ThemeInfo info = new ThemeInfo(id);
				boolean found = false;
				File infoFile = ResourceManager.getExternalThemeInfo(id);
				if (infoFile != null && infoFile.exists()) {
					try (BufferedReader br = new BufferedReader(
							new InputStreamReader(new FileInputStream(infoFile), "UTF-8"))) {
						parseThemeInfo(info, br);
						found = true;
					} catch (IOException e) {
						System.out.println("Could not read " + infoFile + ": " + e.getMessage());
					}
				} else {
					try (InputStream is = ResourceManager.class.getResourceAsStream(id + "/themesinfo.txt")) {
						if (is != null) {
							parseThemeInfo(info, new BufferedReader(new InputStreamReader(is, "UTF-8")));
							found = true;
						}
					} catch (IOException e) {
						System.out.println("Could not read bundled " + id + "/themesinfo.txt: " + e.getMessage());
					}
				}
				if (found) result.add(info);
			}
		}

		if (result.isEmpty()) {
			result.addAll(loadBundledThemeInfo());
		}

		String savedTheme = EditorApp.getTheme();
		boolean found = false;
		for (ThemeInfo t : result) {
			if (t.id.equals(savedTheme)) { found = true; break; }
		}
		if (!found) {
			System.out.println("Theme '" + savedTheme + "' couldn't be loaded (did you delete the folder?)");
			EditorApp.setTheme("default");
		}

		return result;
	}


	private static void applyInfoLine(ThemeInfo t, String line) {
		int eq = line.indexOf('=');
		if (eq < 0) return;
		String key = line.substring(0, eq).trim().toLowerCase();
		String val = line.substring(eq + 1).trim();
		switch (key) {
			case "name":               t.name        = val; break;
			case "author":             t.author      = val; break;
			case "description":        t.description = val; break;
			case "theme":              t.lightUI     = val.equalsIgnoreCase("light"); break;
			case "panel-color":        tryDecode(val, c -> t.panelColor  = c); break;
			case "splash-color":       tryDecode(val, c -> t.splashColor = c); break;
			case "background":         tryDecode(val, c -> t.bg           = c); break;
			case "background-secondary": tryDecode(val, c -> t.bgLighter  = c); break;
			case "background-input":   tryDecode(val, c -> t.bgField      = c); break;
			case "text-color":         tryDecode(val, c -> t.fg           = c); break;
			case "text-color-bright":  tryDecode(val, c -> t.fgBright     = c); break;
			case "selection-color":    tryDecode(val, c -> t.selection    = c); break;
			case "border-color":       tryDecode(val, c -> t.border       = c); break;
			case "button-color":       tryDecode(val, c -> t.buttonBg     = c); break;
			case "text-color-inactive": tryDecode(val, c -> t.inactiveFg  = c); break;
			case "map-outline":        tryDecode(val, c -> t.mapOutline   = c); break;
			case "tsc-tag-color":      tryDecode(val, c -> t.tscTagFg     = c); break;
			case "tsc-tag-background": tryDecode(val, c -> t.tscTagBg     = c); break;
			case "scroll-bar":         tryDecode(val, c -> t.scrollBar    = c); break;
			case "scroll-bg":          tryDecode(val, c -> t.scrollBg     = c); break;
			case "script-bg":          tryDecode(val, c -> t.scriptBg     = c); break;
			case "panel-outline":      tryDecode(val, c -> t.panelOutline = c); break;
			case "divider-col":        tryDecode(val, c -> t.dividerCol   = c); break;
		}
	}

	private static void tryDecode(String val, java.util.function.Consumer<Color> setter) {
		try { setter.accept(Color.decode(val)); } catch (NumberFormatException ignored) {}
	}

	private static List<ThemeInfo> loadBundledThemeInfo() {
		List<ThemeInfo> result = new ArrayList<>();

		List<String> orderedIds = new ArrayList<>();
		File extThemeId = ResourceManager.getExternalThemeId();
		if (extThemeId != null && extThemeId.exists()) {
			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(extThemeId), "UTF-8"))) {
				String line;
				while ((line = br.readLine()) != null) {
					line = line.trim();
					if (!line.isEmpty() && !line.startsWith("#")) orderedIds.add(line);
				}
			} catch (IOException e) {
				System.out.println("Could not read " + extThemeId + ": " + e.getMessage());
			}
		}
		if (orderedIds.isEmpty()) {
			try (InputStream is = ResourceManager.class.getResourceAsStream("themeid.txt")) {
				if (is != null) {
					BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					String line;
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (!line.isEmpty() && !line.startsWith("#")) orderedIds.add(line);
					}
				}
			} catch (IOException e) {
				System.out.println("Could not read bundled themeid.txt: " + e.getMessage());
			}
		}

		if (orderedIds.isEmpty()) {
			orderedIds.add("default");
		}

		for (String id : orderedIds) {
			ThemeInfo info = new ThemeInfo(id);
			try (InputStream is = ResourceManager.class.getResourceAsStream(id + "/themesinfo.txt")) {
				if (is != null) {
					parseThemeInfo(info, new BufferedReader(new InputStreamReader(is, "UTF-8")));
				}
			} catch (IOException e) {
				System.out.println("Could not read bundled " + id + "/themesinfo.txt: " + e.getMessage());
			}
			result.add(info);
		}

		if (result.isEmpty()) {
			ThemeInfo def = new ThemeInfo("default");
			def.name        = "Default";
			def.panelColor  = Color.decode("#2b2b2b");
			def.splashColor = Color.decode("#101a2e");
			result.add(def);
		}

		return result;
	}
 
	private static void parseThemeInfo(ThemeInfo info, BufferedReader br) throws IOException {
		String line;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.isEmpty() || line.startsWith("#")) continue;
			if (line.contains("=")) applyInfoLine(info, line);
		}
	}
}
