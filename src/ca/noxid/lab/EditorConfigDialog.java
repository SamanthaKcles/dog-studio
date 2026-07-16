package ca.noxid.lab;

import ca.noxid.lab.rsrc.ResourceManager;
import ca.noxid.lab.script.TscCommand;
import ca.noxid.uiComponents.BgPanel;
import com.carrotlord.string.StrTools;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.prefs.Preferences;

public class EditorConfigDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private static final String PREF_PLAY_SFX = "play_sfx";
	private static final String PREF_WARN_SCRIPT_ERRORS = "warn_script_errors";
	private static final String PREF_WARN_UNRECOGNIZED_COMMANDS = "warn_unrecognized_commands";
	private static final String PREF_AUTUMNAL_LAB = "autumnal_lab";
	private static final String PREF_ENABLE_CK = "enable_ck";
	private static final String PREF_DISABLE_EDITOR_RECT = "disable_editor_rect";
	private static final String PREF_SCROLL_SPEED = "scroll_speed";

	public static String getScrollSpeed() {
		Preferences prefs = Preferences.userNodeForPackage(EditorApp.class);
		return prefs.get(PREF_SCROLL_SPEED, "medium");
	}

	private static void setScrollSpeed(String speed) {
		Preferences prefs = Preferences.userNodeForPackage(EditorApp.class);
		prefs.put(PREF_SCROLL_SPEED, speed);
	}

	public static int getScrollUnitIncrement() {
		switch (getScrollSpeed()) {
			case "high":   return 30;
			case "medium": return 20;
			default:       return 10;
		}
	}

	public static boolean isSfxEnabled() {
		Preferences prefs = Preferences.userNodeForPackage(EditorApp.class);
		return prefs.getBoolean(PREF_PLAY_SFX, true);
	}

	private static void setSfxEnabled(boolean enabled) {
		Preferences prefs = Preferences.userNodeForPackage(EditorApp.class);
		prefs.putBoolean(PREF_PLAY_SFX, enabled);
	}

	public static boolean isWarnScriptErrors() {
		Preferences prefs = Preferences.userNodeForPackage(EditorApp.class);
		return prefs.getBoolean(PREF_WARN_SCRIPT_ERRORS, true);
	}

	private static void setWarnScriptErrors(boolean enabled) {
		Preferences prefs = Preferences.userNodeForPackage(EditorApp.class);
		prefs.putBoolean(PREF_WARN_SCRIPT_ERRORS, enabled);
	}

	public static boolean isWarnUnrecognizedCommands() {
		Preferences prefs = Preferences.userNodeForPackage(EditorApp.class);
		return prefs.getBoolean(PREF_WARN_UNRECOGNIZED_COMMANDS, true);
	}

	private static void setWarnUnrecognizedCommands(boolean enabled) {
		Preferences prefs = Preferences.userNodeForPackage(EditorApp.class);
		prefs.putBoolean(PREF_WARN_UNRECOGNIZED_COMMANDS, enabled);
	}

	public static boolean isAutumnalLabEnabled() {
		Preferences prefs = Preferences.userNodeForPackage(EditorApp.class);
		return prefs.getBoolean(PREF_AUTUMNAL_LAB, false);
	}

	private static void setAutumnalLabEnabled(boolean enabled) {
		Preferences prefs = Preferences.userNodeForPackage(EditorApp.class);
		prefs.putBoolean(PREF_AUTUMNAL_LAB, enabled);
	}

	public static boolean isColorAndKeyEnabled() {
		Preferences prefs = Preferences.userNodeForPackage(EditorApp.class);
		return prefs.getBoolean(PREF_ENABLE_CK, false);
	}

	private static void setColorAndKeyEnabled(boolean enabled) {
		Preferences prefs = Preferences.userNodeForPackage(EditorApp.class);
		prefs.putBoolean(PREF_ENABLE_CK, enabled);
	}

	public static boolean isEditorRectDisabled() {
		Preferences prefs = Preferences.userNodeForPackage(EditorApp.class);
		return prefs.getBoolean(PREF_DISABLE_EDITOR_RECT, false);
	}

	private static void setEditorRectDisabled(boolean disabled) {
		Preferences prefs = Preferences.userNodeForPackage(EditorApp.class);
		prefs.putBoolean(PREF_DISABLE_EDITOR_RECT, disabled);
	}

	private JList<String> categoryList;
	private JPanel contentPanel;
	private ResourceManager iMan;
	private CardLayout cardLayout;
	
	private JList<String> tscCommandList;
	private DefaultListModel<String> tscListModel;
	private JTextField nameField;
	private JComboBox<Integer> opsCombo;
	private JComboBox<String>[] idCombos;
	private JTextField abbField;
	private JTextArea descArea;
	private Map<String, TscCommandData> tscCommands;
	private boolean loadingTscDetails = false;

	private boolean requiresRestart = false;

	private void markRestartRequired() {
		requiresRestart = true;
	}
	private DefaultListModel<String> musicListModel;
	private DefaultListModel<String> sfxListModel;
	private DefaultListModel<String> endListModel;
	private DefaultListModel<String> mapBossListModel;
	private DefaultListModel<String> bgTypeListModel;
	private Map<String, TscCommandData> tscCommandsSnapshot;
	private List<String> musicListSnapshot;
	private List<String> sfxListSnapshot;
	
	private static final String[] ID_TYPES = {
		"None", "Arms", "Ammo", "Direction", "Event", "Equip", "Face", "Flag", 
		"Graphic", "Illustration", "Item", "Map", "Music", "NPC (specific)", 
		"NPC Type", "Sound", "Tile", "X Coord", "Y Coord", "Number", "Ticks", "String"
	};
	
	public EditorConfigDialog(Frame parent, ResourceManager iMan) {
		super(parent, "Editor Configuration", true);
		this.iMan = iMan;
		if (ResourceManager.cursor != null) this.setCursor(ResourceManager.cursor);
		
		setLayout(new BorderLayout());
		
		String[] categories = {"General Config", "TSC Commands", "End Commands", "Music List", "SFX List", "Equip List", "Map Bosses", "Background Types"};
		categoryList = new JList<>(categories);
		categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		categoryList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					switchCategory(categoryList.getSelectedValue());
				}
			}
		});
		
		JPanel leftPanel = new JPanel(new GridBagLayout());
		GridBagConstraints lc = new GridBagConstraints();
		lc.gridx = 0;
		lc.gridy = 0;
		lc.fill = GridBagConstraints.BOTH;
		lc.weightx = 1.0;
		lc.weighty = 1.0;
		lc.insets = new Insets(0, 0, 0, 5);
		JScrollPane leftScroll = new JScrollPane(categoryList);
		leftScroll.setMinimumSize(new Dimension(130, 120));
		leftScroll.setPreferredSize(new Dimension(130, 120));
		leftPanel.add(leftScroll, lc);
		add(leftPanel, BorderLayout.WEST);
		
		cardLayout = new CardLayout();
		contentPanel = new JPanel(cardLayout);
		contentPanel.setPreferredSize(new Dimension(600, 400));
		
		initializePanels();
		
		add(contentPanel, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel(new BorderLayout());
		
		JPanel buttonPanel = new JPanel();
		JButton saveButton = new JButton("Save");
		JButton closeButton = new JButton("Cancel");
		
		saveButton.addActionListener(e -> {
			int result = JOptionPane.showConfirmDialog(this, "Save all changes?", "Confirm Save", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				saveAllChanges();
				if (requiresRestart) {
					int restartResult = JOptionPane.showConfirmDialog(
							this,
							"Changes saved. A restart is required for some changes to take effect. Restart now?",
							"Restart required",
							JOptionPane.YES_NO_OPTION);
					dispose();
					if (restartResult == JOptionPane.YES_OPTION) {
						EditorApp.restartApplication();
					}
				} else {
					JOptionPane.showMessageDialog(this, "Changes saved.", "Saved", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
			}
		});
		
		closeButton.addActionListener(e -> {
			int result = JOptionPane.showConfirmDialog(this, "Discard all unsaved changes?", "Confirm Cancel", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				restoreOriginalState();
				dispose();
			}
		});
		
		buttonPanel.add(saveButton);
		buttonPanel.add(closeButton);
		bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		add(bottomPanel, BorderLayout.SOUTH);
		
		pack();
		setSize(800, 600);
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	
	private void initializePanels() {
		contentPanel.add(createGeneralConfigPanel(), "General Config");
		contentPanel.add(createTscCommandPanel(), "TSC Commands");
		contentPanel.add(createSimpleListPanel("End Commands", "endlist.txt"), "End Commands");
		contentPanel.add(createMusicListPanel(), "Music List");
		contentPanel.add(createSfxListPanel(), "SFX List");
		contentPanel.add(createSimpleListPanel("Equip List", "equipList.txt"), "Equip List");
		contentPanel.add(createSimpleListPanel("Map Bosses", "mapBosses.txt"), "Map Bosses");
		contentPanel.add(createSimpleListPanel("Background Types", "backgroundTypes.txt"), "Background Types");
		snapshotOriginalState();
	}
	
	private void switchCategory(String category) {
		if (category != null) {
			cardLayout.show(contentPanel, category);
		}
	}
	
	private JPanel createGeneralConfigPanel() {
		JPanel panel = new BgPanel(iMan.getImg(ResourceManager.rsrcBgBlue));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(10, 10, 4, 10);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;

		JCheckBox playSfxCheck = new JCheckBox("Play SFX");
		playSfxCheck.setOpaque(false);
		playSfxCheck.setSelected(isSfxEnabled());
		playSfxCheck.addActionListener(e -> setSfxEnabled(playSfxCheck.isSelected()));
		panel.add(playSfxCheck, c);

		c.gridy++;
		JCheckBox warnScriptErrorsCheck = new JCheckBox("Warn Script Errors");
		warnScriptErrorsCheck.setOpaque(false);
		warnScriptErrorsCheck.setSelected(isWarnScriptErrors());
		warnScriptErrorsCheck.addActionListener(e -> setWarnScriptErrors(warnScriptErrorsCheck.isSelected()));
		panel.add(warnScriptErrorsCheck, c);

		c.gridy++;
		JCheckBox warnUnrecognizedCommandsCheck = new JCheckBox("Warn Unrecognized Commands");
		warnUnrecognizedCommandsCheck.setOpaque(false);
		warnUnrecognizedCommandsCheck.setSelected(isWarnUnrecognizedCommands());
		warnUnrecognizedCommandsCheck.addActionListener(e -> setWarnUnrecognizedCommands(warnUnrecognizedCommandsCheck.isSelected()));
		panel.add(warnUnrecognizedCommandsCheck, c);

		c.gridy++;
		JCheckBox autumnalLabCheck = new JCheckBox("Autumnal Lab Mode");
		autumnalLabCheck.setOpaque(false);
		autumnalLabCheck.setSelected(isAutumnalLabEnabled());
		autumnalLabCheck.addActionListener(e -> {
			setAutumnalLabEnabled(autumnalLabCheck.isSelected());
			markRestartRequired();
		});
		panel.add(autumnalLabCheck, c);

		c.gridy++;
		JCheckBox colorAndKeyCheck = new JCheckBox("Color Presets & Key Items");
		colorAndKeyCheck.setOpaque(false);
		if (EditorApp.isCsdhMode()) {
			colorAndKeyCheck.setSelected(true);
			colorAndKeyCheck.setEnabled(false);
			colorAndKeyCheck.setToolTipText("Bro");
		} else {
			colorAndKeyCheck.setSelected(isColorAndKeyEnabled());
			colorAndKeyCheck.addActionListener(e -> {
				setColorAndKeyEnabled(colorAndKeyCheck.isSelected());
				markRestartRequired();
			});
		}
		panel.add(colorAndKeyCheck, c);

		c.gridy++;
		JCheckBox disableEditorRectCheck = new JCheckBox("Disable Editor.rect");
		disableEditorRectCheck.setOpaque(false);
		disableEditorRectCheck.setSelected(isEditorRectDisabled());
		disableEditorRectCheck.addActionListener(e -> setEditorRectDisabled(disableEditorRectCheck.isSelected()));
		panel.add(disableEditorRectCheck, c);

		// lethrys slop
		JPanel scrollPanel = new JPanel();
		scrollPanel.setOpaque(false);
		scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));

		JLabel scrollLabel = new JLabel("Map Scroll Speed");
		scrollLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollPanel.add(scrollLabel);
		scrollPanel.add(Box.createVerticalStrut(4));

		ButtonGroup scrollGroup = new ButtonGroup();
		JRadioButton highRadio   = new JRadioButton("High");
		JRadioButton mediumRadio = new JRadioButton("Medium");
		JRadioButton slowRadio   = new JRadioButton("Slow");
		highRadio.setOpaque(false);
		mediumRadio.setOpaque(false);
		slowRadio.setOpaque(false);
		highRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
		mediumRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
		slowRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollGroup.add(highRadio);
		scrollGroup.add(mediumRadio);
		scrollGroup.add(slowRadio);

		String currentSpeed = getScrollSpeed();
		if ("high".equals(currentSpeed))       highRadio.setSelected(true);
		else if ("slow".equals(currentSpeed))  slowRadio.setSelected(true);
		else                                   mediumRadio.setSelected(true);

		highRadio.addActionListener(e   -> { setScrollSpeed("high");   markRestartRequired(); });
		mediumRadio.addActionListener(e -> { setScrollSpeed("medium"); markRestartRequired(); });
		slowRadio.addActionListener(e   -> { setScrollSpeed("slow");   markRestartRequired(); });

		scrollPanel.add(highRadio);
		scrollPanel.add(mediumRadio);
		scrollPanel.add(slowRadio);

		GridBagConstraints sc = new GridBagConstraints();
		sc.anchor  = GridBagConstraints.NORTHWEST;
		sc.insets  = new Insets(10, 20, 4, 10);
		sc.gridx   = 1;
		sc.gridy   = 0;
		sc.gridheight = 5;
		sc.weightx = 1.0;
		sc.weighty = 0;
		panel.add(scrollPanel, sc);

		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.VERTICAL;
		panel.add(Box.createGlue(), c);

		return panel;
	}

	@SuppressWarnings("unchecked")
	private JPanel createTscCommandPanel() {
		JPanel panel = new BgPanel(iMan.getImg(ResourceManager.rsrcBgBlue));
		panel.setLayout(new BorderLayout());
		
		tscCommands = new LinkedHashMap<>();
		loadTscCommands();
		
		tscListModel = new DefaultListModel<>();
		for (String cmd : tscCommands.keySet()) {
			tscListModel.addElement(cmd);
		}
		tscListModel.addListDataListener(new javax.swing.event.ListDataListener() {
			public void intervalAdded(javax.swing.event.ListDataEvent e) { markRestartRequired(); }
			public void intervalRemoved(javax.swing.event.ListDataEvent e) { markRestartRequired(); }
			public void contentsChanged(javax.swing.event.ListDataEvent e) { markRestartRequired(); }
		});
		tscCommandList = new JList<>(tscListModel);
		tscCommandList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tscCommandList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				loadTscCommandDetails(tscCommandList.getSelectedValue());
			}
		});
		
		JPanel listPanel = new JPanel(new BorderLayout());
		JScrollPane tscScrollPane = new JScrollPane(tscCommandList);
		tscScrollPane.setPreferredSize(new Dimension(200, 400));
		listPanel.add(tscScrollPane, BorderLayout.CENTER);
		
		JPanel listButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
		JButton addButton = new JButton("Add");
		JButton deleteButton = new JButton("Delete");
		Dimension btnSize = new Dimension(95, 25);
		addButton.setPreferredSize(btnSize);
		deleteButton.setPreferredSize(btnSize);
		
		addButton.addActionListener(e -> {
			String cmd = JOptionPane.showInputDialog(this, "Enter 3-character command (e.g., ABC):");
			if (cmd != null && cmd.length() == 3) {
				cmd = "<" + cmd.toUpperCase();
				if (!tscCommands.containsKey(cmd)) {
					TscCommandData data = new TscCommandData();
					data.name = cmd;
					data.ops = 0;
					data.ids = new String[6];
					Arrays.fill(data.ids, "");
					data.abb = "";
					data.desc = "";
					tscCommands.put(cmd, data);
					
					int index = 0;
					for (int i = 0; i < tscListModel.size(); i++) {
						if (cmd.compareTo(tscListModel.get(i)) < 0) {
							index = i;
							break;
						}
						index = i + 1;
					}
					tscListModel.add(index, cmd);
					tscCommandList.setSelectedIndex(index);
				} else {
					JOptionPane.showMessageDialog(this, "Command already exists!");
				}
			}
		});
		
		deleteButton.addActionListener(e -> {
			String selected = tscCommandList.getSelectedValue();
			if (selected != null) {
				int result = JOptionPane.showConfirmDialog(this, "Delete command " + selected + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					tscCommands.remove(selected);
					tscListModel.removeElement(selected);
				}
			}
		});
		
		listButtonPanel.add(addButton);
		listButtonPanel.add(deleteButton);
		listPanel.add(listButtonPanel, BorderLayout.SOUTH);
		
		panel.add(listPanel, BorderLayout.WEST);
		
		JPanel detailsPanel = new JPanel();
		detailsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 0; c.gridy = 0;
		detailsPanel.add(new JLabel("Name"), c);
		c.gridx = 1; c.gridwidth = 2;
		nameField = new JTextField(20);
		detailsPanel.add(nameField, c);
		
		c.gridx = 0; c.gridy = 1; c.gridwidth = 1;
		detailsPanel.add(new JLabel("Ops."), c);
		c.gridx = 1;
		opsCombo = new JComboBox<>(new Integer[]{0, 1, 2, 3, 4, 5, 6});
		opsCombo.addActionListener(e -> updateIdComboStates());
		detailsPanel.add(opsCombo, c);
		
		c.gridx = 0; c.gridy = 2;
		detailsPanel.add(new JLabel("IDs"), c);
		c.gridx = 1; c.gridwidth = 2;
		JPanel idsPanel = new JPanel(new GridLayout(2, 3, 5, 5));
		idCombos = new JComboBox[6];
		String[] idLabels = {"W", "X", "Y", "Z", "Q", "R"};
		for (int i = 0; i < 6; i++) {
			final int idx = i;
			idCombos[i] = new JComboBox<>(ID_TYPES);
			idCombos[i].setRenderer(new DefaultListCellRenderer() {
				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					if (value != null && value.equals("None")) {
						value = idLabels[idx] + idLabels[idx] + idLabels[idx] + idLabels[idx];
					}
					return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				}
			});
			idsPanel.add(idCombos[i]);
		}
		detailsPanel.add(idsPanel, c);
		
		c.gridx = 0; c.gridy = 3; c.gridwidth = 1;
		detailsPanel.add(new JLabel("Abb."), c);
		c.gridx = 1; c.gridwidth = 2;
		abbField = new JTextField(20);
		detailsPanel.add(abbField, c);
		
		c.gridx = 0; c.gridy = 4; c.gridwidth = 1;
		detailsPanel.add(new JLabel("Description"), c);
		c.gridx = 1; c.gridwidth = 2; c.fill = GridBagConstraints.BOTH; c.weighty = 1.0;
		descArea = new JTextArea(5, 20);
		descArea.setLineWrap(true);
		descArea.setWrapStyleWord(true);
		detailsPanel.add(new JScrollPane(descArea), c);
		
		javax.swing.event.DocumentListener tscAutoSave = new javax.swing.event.DocumentListener() {
			public void insertUpdate(javax.swing.event.DocumentEvent e) { saveTscCommandDetails(); }
			public void removeUpdate(javax.swing.event.DocumentEvent e) { saveTscCommandDetails(); }
			public void changedUpdate(javax.swing.event.DocumentEvent e) { saveTscCommandDetails(); }
		};
		nameField.getDocument().addDocumentListener(tscAutoSave);
		abbField.getDocument().addDocumentListener(tscAutoSave);
		descArea.getDocument().addDocumentListener(tscAutoSave);
		opsCombo.addActionListener(e -> saveTscCommandDetails());
		for (JComboBox<String> idCombo : idCombos) {
			idCombo.addActionListener(e -> saveTscCommandDetails());
		}
		
		panel.add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
		
		return panel;
	}
	
	private JPanel createMusicListPanel() {
		JPanel panel = new BgPanel(iMan.getImg(ResourceManager.rsrcBgBlue));
		panel.setLayout(new BorderLayout());
		
		musicListModel = new DefaultListModel<>();
		loadMusicList();
		musicListModel.addListDataListener(new javax.swing.event.ListDataListener() {
			public void intervalAdded(javax.swing.event.ListDataEvent e) { markRestartRequired(); }
			public void intervalRemoved(javax.swing.event.ListDataEvent e) { markRestartRequired(); }
			public void contentsChanged(javax.swing.event.ListDataEvent e) { markRestartRequired(); }
		});
		
		JList<String> list = new JList<>(musicListModel);
		panel.add(new JScrollPane(list), BorderLayout.CENTER);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		JTextField nameField = new JTextField(30);
		Dimension fieldSize = new Dimension(300, 25);
		nameField.setMaximumSize(fieldSize);
		nameField.setPreferredSize(fieldSize);
		JLabel songLabel = new JLabel("Song Name");
		songLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
		rightPanel.add(songLabel);
		rightPanel.add(nameField);
		rightPanel.add(Box.createVerticalStrut(10));
		
		JButton addNew = new JButton("Add");
		JButton delete = new JButton("Delete");
		JButton moveUp = new JButton("Move Up");
		JButton moveDown = new JButton("Move Down");
		Dimension btnSize = new Dimension(300, 25);
		addNew.setPreferredSize(btnSize);
		addNew.setMaximumSize(btnSize);
		addNew.setAlignmentX(Component.LEFT_ALIGNMENT);
		delete.setPreferredSize(btnSize);
		delete.setMaximumSize(btnSize);
		delete.setAlignmentX(Component.LEFT_ALIGNMENT);
		moveUp.setPreferredSize(btnSize);
		moveUp.setMaximumSize(btnSize);
		moveUp.setAlignmentX(Component.LEFT_ALIGNMENT);
		moveDown.setPreferredSize(btnSize);
		moveDown.setMaximumSize(btnSize);
		moveDown.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		final boolean[] updatingFromSelection = {false};
		list.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && list.getSelectedIndex() >= 0) {
				updatingFromSelection[0] = true;
				String fullLine = musicListModel.get(list.getSelectedIndex());
				String songName = extractMusicName(fullLine);
				nameField.setText(songName);
				updatingFromSelection[0] = false;
			}
		});
		nameField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			private void autoUpdate() {
				if (updatingFromSelection[0]) return;
				int idx = list.getSelectedIndex();
				if (idx >= 0) {
					String newLine = String.format("%04d %s", idx, nameField.getText());
					musicListModel.set(idx, newLine);
				}
			}
			public void insertUpdate(javax.swing.event.DocumentEvent e) { autoUpdate(); }
			public void removeUpdate(javax.swing.event.DocumentEvent e) { autoUpdate(); }
			public void changedUpdate(javax.swing.event.DocumentEvent e) { autoUpdate(); }
		});
		
		addNew.addActionListener(e -> {
			String name = JOptionPane.showInputDialog(this, "Enter song name:");
			if (name != null && !name.trim().isEmpty()) {
				int idx = musicListModel.size();
				String newLine = String.format("%04d %s", idx, name);
				musicListModel.addElement(newLine);
				list.setSelectedIndex(idx);
			}
		});
		
		delete.addActionListener(e -> {
			int idx = list.getSelectedIndex();
			if (idx >= 0) {
				int result = JOptionPane.showConfirmDialog(this, "Delete this entry?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					musicListModel.remove(idx);
					rebuildMusicList();
					if (idx < musicListModel.size()) {
						list.setSelectedIndex(idx);
					} else if (musicListModel.size() > 0) {
						list.setSelectedIndex(musicListModel.size() - 1);
					}
				}
			}
		});
		
		moveUp.addActionListener(e -> {
			int idx = list.getSelectedIndex();
			if (idx > 0) {
				String item = musicListModel.remove(idx);
				musicListModel.add(idx - 1, item);
				list.setSelectedIndex(idx - 1);
				rebuildMusicList();
			}
		});
		
		moveDown.addActionListener(e -> {
			int idx = list.getSelectedIndex();
			if (idx >= 0 && idx < musicListModel.size() - 1) {
				String item = musicListModel.remove(idx);
				musicListModel.add(idx + 1, item);
				list.setSelectedIndex(idx + 1);
				rebuildMusicList();
			}
		});
		
		rightPanel.add(addNew);
		rightPanel.add(Box.createVerticalStrut(4));
		rightPanel.add(delete);
		rightPanel.add(Box.createVerticalStrut(4));
		rightPanel.add(moveUp);
		rightPanel.add(Box.createVerticalStrut(4));
		rightPanel.add(moveDown);
		
		panel.add(rightPanel, BorderLayout.EAST);
		
		return panel;
	}
	
	private JPanel createSfxListPanel() {
		JPanel panel = new BgPanel(iMan.getImg(ResourceManager.rsrcBgBlue));
		panel.setLayout(new BorderLayout());
		
		sfxListModel = new DefaultListModel<>();
		loadSfxList();
		sfxListModel.addListDataListener(new javax.swing.event.ListDataListener() {
			public void intervalAdded(javax.swing.event.ListDataEvent e) { markRestartRequired(); }
			public void intervalRemoved(javax.swing.event.ListDataEvent e) { markRestartRequired(); }
			public void contentsChanged(javax.swing.event.ListDataEvent e) { markRestartRequired(); }
		});
		
		JList<String> list = new JList<>(sfxListModel);
		panel.add(new JScrollPane(list), BorderLayout.CENTER);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		JTextField nameField = new JTextField(30);
		Dimension fieldSize = new Dimension(300, 25);
		nameField.setMaximumSize(fieldSize);
		nameField.setPreferredSize(fieldSize);
		JLabel sfxLabel = new JLabel("SFX Name");
		sfxLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
		rightPanel.add(sfxLabel);
		rightPanel.add(nameField);
		rightPanel.add(Box.createVerticalStrut(10));
		
		JButton addNew = new JButton("Add");
		JButton delete = new JButton("Delete");
		JButton moveUp = new JButton("Move Up");
		JButton moveDown = new JButton("Move Down");
		Dimension btnSize = new Dimension(300, 25);
		addNew.setPreferredSize(btnSize);
		addNew.setMaximumSize(btnSize);
		addNew.setAlignmentX(Component.LEFT_ALIGNMENT);
		delete.setPreferredSize(btnSize);
		delete.setMaximumSize(btnSize);
		delete.setAlignmentX(Component.LEFT_ALIGNMENT);
		moveUp.setPreferredSize(btnSize);
		moveUp.setMaximumSize(btnSize);
		moveUp.setAlignmentX(Component.LEFT_ALIGNMENT);
		moveDown.setPreferredSize(btnSize);
		moveDown.setMaximumSize(btnSize);
		moveDown.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		final boolean[] sfxUpdatingFromSelection = {false};
		list.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && list.getSelectedIndex() >= 0) {
				sfxUpdatingFromSelection[0] = true;
				String fullLine = sfxListModel.get(list.getSelectedIndex());
				String sfxName = extractSfxName(fullLine);
				nameField.setText(sfxName);
				sfxUpdatingFromSelection[0] = false;
			}
		});
		nameField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			private void autoUpdate() {
				if (sfxUpdatingFromSelection[0]) return;
				int idx = list.getSelectedIndex();
				if (idx >= 0) {
					String newLine = String.format("%d - %s", idx, nameField.getText());
					sfxListModel.set(idx, newLine);
				}
			}
			public void insertUpdate(javax.swing.event.DocumentEvent e) { autoUpdate(); }
			public void removeUpdate(javax.swing.event.DocumentEvent e) { autoUpdate(); }
			public void changedUpdate(javax.swing.event.DocumentEvent e) { autoUpdate(); }
		});
		
		addNew.addActionListener(e -> {
			String name = JOptionPane.showInputDialog(this, "Enter SFX name:");
			if (name != null && !name.trim().isEmpty()) {
				int idx = sfxListModel.size();
				String newLine = String.format("%d - %s", idx, name);
				sfxListModel.addElement(newLine);
				list.setSelectedIndex(idx);
			}
		});
		
		delete.addActionListener(e -> {
			int idx = list.getSelectedIndex();
			if (idx >= 0) {
				int result = JOptionPane.showConfirmDialog(this, "Delete this entry?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					sfxListModel.remove(idx);
					rebuildSfxList();
					if (idx < sfxListModel.size()) {
						list.setSelectedIndex(idx);
					} else if (sfxListModel.size() > 0) {
						list.setSelectedIndex(sfxListModel.size() - 1);
					}
				}
			}
		});
		
		moveUp.addActionListener(e -> {
			int idx = list.getSelectedIndex();
			if (idx > 0) {
				String item = sfxListModel.remove(idx);
				sfxListModel.add(idx - 1, item);
				list.setSelectedIndex(idx - 1);
				rebuildSfxList();
			}
		});
		
		moveDown.addActionListener(e -> {
			int idx = list.getSelectedIndex();
			if (idx >= 0 && idx < sfxListModel.size() - 1) {
				String item = sfxListModel.remove(idx);
				sfxListModel.add(idx + 1, item);
				list.setSelectedIndex(idx + 1);
				rebuildSfxList();
			}
		});
		
		rightPanel.add(addNew);
		rightPanel.add(Box.createVerticalStrut(4));
		rightPanel.add(delete);
		rightPanel.add(Box.createVerticalStrut(4));
		rightPanel.add(moveUp);
		rightPanel.add(Box.createVerticalStrut(4));
		rightPanel.add(moveDown);
		
		panel.add(rightPanel, BorderLayout.EAST);
		
		return panel;
	}
	
	private JPanel createSimpleListPanel(String title, String filename) {
		JPanel panel = new BgPanel(iMan.getImg(ResourceManager.rsrcBgBlue));
		panel.setLayout(new BorderLayout());
		
		DefaultListModel<String> model = new DefaultListModel<>();
		loadSimpleList(filename, model);
		
		if (filename.equals("endlist.txt")) endListModel = model;
		else if (filename.equals("mapBosses.txt")) mapBossListModel = model;
		else if (filename.equals("backgroundTypes.txt")) bgTypeListModel = model;

		model.addListDataListener(new javax.swing.event.ListDataListener() {
			public void intervalAdded(javax.swing.event.ListDataEvent e) { markRestartRequired(); }
			public void intervalRemoved(javax.swing.event.ListDataEvent e) { markRestartRequired(); }
			public void contentsChanged(javax.swing.event.ListDataEvent e) { markRestartRequired(); }
		});
		JList<String> list = new JList<>(model);
		panel.add(new JScrollPane(list), BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		JButton addButton = new JButton("Add");
		JButton removeButton = new JButton("Delete");
		Dimension btnSize = new Dimension(80, 25);
		addButton.setPreferredSize(btnSize);
		removeButton.setPreferredSize(btnSize);
		
		addButton.addActionListener(e -> {
			String item = JOptionPane.showInputDialog(this, "Enter new item:");
			if (item != null && !item.trim().isEmpty()) {
				model.addElement(item);
			}
		});
		
		removeButton.addActionListener(e -> {
			int idx = list.getSelectedIndex();
			if (idx >= 0) {
				model.remove(idx);
			}
		});
		
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private void loadTscCommands() {
		File fileToLoad = isAutumnalLabEnabled() ? new File("aut/tsc_list.txt") : new File("tsc_list.txt");
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileToLoad))) {
			String line;
			boolean inCommands = false;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("[CE_TSC]")) {
					inCommands = true;
					continue;
				}
				if (inCommands && line.startsWith("<")) {
					String[] parts = line.split("\\t");
					if (parts.length >= 5) {
						TscCommandData data = new TscCommandData();
						data.name = parts[0];
						data.ops = Integer.parseInt(parts[1]);
						data.abb = parts[3];
						data.desc = parts[4];
						
						// Parse IDs
						String idStr = parts[2];
						data.ids = new String[6];
						for (int i = 0; i < 6 && i < idStr.length(); i++) {
							char c = idStr.charAt(i);
							data.ids[i] = charToIdType(c);
						}
						for (int i = idStr.length(); i < 6; i++) {
							data.ids[i] = "";
						}
						
						tscCommands.put(data.name, data);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String charToIdType(char c) {
		switch (c) {
			case 'a': return "Arms";
			case 'A': return "Ammo";
			case 'd': return "Direction";
			case 'e': return "Event";
			case 'E': return "Equip";
			case 'f': return "Face";
			case 'F': return "Flag";
			case 'g': return "Graphic";
			case 'l': return "Illustration";
			case 'i': return "Item";
			case 'm': return "Map";
			case 'u': return "Music";
			case 'N': return "NPC (specific)";
			case 'n': return "NPC Type";
			case 's': return "Sound";
			case 't': return "Tile";
			case 'x': return "X Coord";
			case 'y': return "Y Coord";
			case '#': return "Number";
			case '.': return "Ticks";
			case '$': return "String";
			default: return "None";
		}
	}
	
	private char idTypeToChar(String type) {
		if (type == null || type.isEmpty() || type.equals("None")) return '-';
		switch (type) {
			case "Arms": return 'a';
			case "Ammo": return 'A';
			case "Direction": return 'd';
			case "Event": return 'e';
			case "Equip": return 'E';
			case "Face": return 'f';
			case "Flag": return 'F';
			case "Graphic": return 'g';
			case "Illustration": return 'l';
			case "Item": return 'i';
			case "Map": return 'm';
			case "Music": return 'u';
			case "NPC (specific)": return 'N';
			case "NPC Type": return 'n';
			case "Sound": return 's';
			case "Tile": return 't';
			case "X Coord": return 'x';
			case "Y Coord": return 'y';
			case "Number": return '#';
			case "Ticks": return '.';
			case "String": return '$';
			default: return '-';
		}
	}
	
	private void loadTscCommandDetails(String cmd) {
		if (cmd == null) return;
		TscCommandData data = tscCommands.get(cmd);
		if (data != null) {
			loadingTscDetails = true;
			nameField.setText(data.name);
			opsCombo.setSelectedItem(data.ops);
			for (int i = 0; i < 6; i++) {
				if (data.ids[i] != null && !data.ids[i].isEmpty()) {
					idCombos[i].setSelectedItem(data.ids[i]);
				} else {
					idCombos[i].setSelectedIndex(0);
				}
			}
			abbField.setText(data.abb);
			descArea.setText(data.desc);
			updateIdComboStates();
			loadingTscDetails = false;
		}
	}
	
	private void saveTscCommandDetails() {
		if (loadingTscDetails) return;
		String cmd = tscCommandList.getSelectedValue();
		if (cmd == null) return;

		TscCommandData data = tscCommands.get(cmd);
		if (data == null) return;

		String name = nameField.getText().trim();
		int ops = (Integer) opsCombo.getSelectedItem();
		String abb = abbField.getText().trim();
		String desc = descArea.getText().trim();

		if (!name.isEmpty()) data.name = name;
		data.ops = ops;
		for (int i = 0; i < 6; i++) {
			data.ids[i] = (String) idCombos[i].getSelectedItem();
		}
		data.abb = abb;
		data.desc = desc;
		markRestartRequired();
	}
	
	private void updateIdComboStates() {
		int ops = (Integer) opsCombo.getSelectedItem();
		for (int i = 0; i < 6; i++) {
			idCombos[i].setEnabled(i < ops);
			if (i >= ops) {
				idCombos[i].setSelectedItem("None");
			}
		}
	}
	
	private String extractMusicName(String fullLine) {
		if (fullLine.length() > 5) {
			return fullLine.substring(5);
		}
		return "";
	}
	
	private String extractSfxName(String fullLine) {
		int dashIndex = fullLine.indexOf(" - ");
		if (dashIndex >= 0 && dashIndex + 3 < fullLine.length()) {
			return fullLine.substring(dashIndex + 3);
		}
		return "";
	}
	
	private void rebuildMusicList() {
		for (int i = 0; i < musicListModel.size(); i++) {
			String fullLine = musicListModel.get(i);
			String name = extractMusicName(fullLine);
			String newLine = String.format("%04d %s", i, name);
			musicListModel.set(i, newLine);
		}
	}
	
	private void rebuildSfxList() {
		for (int i = 0; i < sfxListModel.size(); i++) {
			String fullLine = sfxListModel.get(i);
			String name = extractSfxName(fullLine);
			String newLine = String.format("%d - %s", i, name);
			sfxListModel.set(i, newLine);
		}
	}
	
	private void loadMusicList() {
		File fileToLoad = new File("musiclist.txt");
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileToLoad))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.trim().isEmpty()) {
					musicListModel.addElement(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadSfxList() {
		File fileToLoad = new File("sfxList.txt");
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileToLoad))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.trim().isEmpty()) {
					sfxListModel.addElement(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadSimpleList(String filename, DefaultListModel<String> model) {
		File fileToLoad = new File(filename);
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileToLoad))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.trim().isEmpty()) {
					model.addElement(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveAllChanges() {
		try {
			saveTscCommands();
			saveMusicList();
			saveSfxList();
			saveSimpleList("endlist.txt", endListModel);
			saveSimpleList("mapBosses.txt", mapBossListModel);
			saveSimpleList("backgroundTypes.txt", bgTypeListModel);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error saving changes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void saveTscCommands() throws IOException {
		for (TscCommandData data : tscCommands.values()) {
			if (data.name == null || data.name.trim().isEmpty()) {
				throw new IOException("Command has empty name");
			}
			if (data.abb == null || data.abb.trim().isEmpty()) {
				throw new IOException("Command " + data.name + " has empty abbreviation");
			}
			if (data.desc == null || data.desc.trim().isEmpty()) {
				throw new IOException("Command " + data.name + " has empty description");
			}
			for (int i = 0; i < data.ops; i++) {
				if (data.ids[i] == null || data.ids[i].equals("None")) {
					throw new IOException("Command " + data.name + " has unset ID types");
				}
			}
		}
		
		List<String> sortedCommands = new ArrayList<>(tscCommands.keySet());
		Collections.sort(sortedCommands);
		boolean autMode = isAutumnalLabEnabled();
		File targetFile = autMode ? new File("aut/tsc_list.txt") : new File("tsc_list.txt");
		if (autMode) targetFile.getParentFile().mkdirs();
		
		List<String> header = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(targetFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("[CE_TSC]")) {
					header.add("[CE_TSC]\t" + tscCommands.size());
					break;
				}
				header.add(line);
			}
		}
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(targetFile))) {
			for (String line : header) {
				bw.write(line);
				bw.newLine();
			}
			
			for (String cmdName : sortedCommands) {
				TscCommandData data = tscCommands.get(cmdName);
				StringBuilder idStr = new StringBuilder();
				for (int i = 0; i < data.ops; i++) {
					idStr.append(idTypeToChar(data.ids[i]));
				}
				for (int i = data.ops; i < 6; i++) {
					idStr.append('-');
				}
				
				bw.write(String.format("%s\t%d\t%s\t%s\t%s", 
					data.name, data.ops, idStr.toString(), data.abb, data.desc));
				bw.newLine();
			}
		}
	}
	
	private void saveMusicList() throws IOException {
		File targetFile = new File("musiclist.txt");
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(targetFile))) {
			for (int i = 0; i < musicListModel.size(); i++) {
				bw.write(musicListModel.get(i));
				bw.newLine();
			}
		}
	}
	
	private void saveSfxList() throws IOException {
		File targetFile = new File("sfxList.txt");
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(targetFile))) {
			for (int i = 0; i < sfxListModel.size(); i++) {
				bw.write(sfxListModel.get(i));
				bw.newLine();
			}
		}
	}
	
	private void saveSimpleList(String filename, DefaultListModel<String> model) throws IOException {
		File targetFile = new File(filename);
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(targetFile))) {
			for (int i = 0; i < model.size(); i++) {
				bw.write(model.get(i));
				bw.newLine();
			}
		}
	}
	private static TscCommandData copyTscCommandData(TscCommandData src) {
		TscCommandData copy = new TscCommandData();
		copy.name = src.name;
		copy.ops  = src.ops;
		copy.ids  = Arrays.copyOf(src.ids, src.ids.length);
		copy.abb  = src.abb;
		copy.desc = src.desc;
		return copy;
	}

	private void snapshotOriginalState() {
		tscCommandsSnapshot = new LinkedHashMap<>();
		for (Map.Entry<String, TscCommandData> e : tscCommands.entrySet()) {
			tscCommandsSnapshot.put(e.getKey(), copyTscCommandData(e.getValue()));
		}
		musicListSnapshot = new ArrayList<>();
		for (int i = 0; i < musicListModel.size(); i++) musicListSnapshot.add(musicListModel.get(i));
		sfxListSnapshot = new ArrayList<>();
		for (int i = 0; i < sfxListModel.size(); i++) sfxListSnapshot.add(sfxListModel.get(i));
	}

	private void restoreOriginalState() {
		tscCommands.clear();
		tscListModel.clear();
		for (Map.Entry<String, TscCommandData> e : tscCommandsSnapshot.entrySet()) {
			tscCommands.put(e.getKey(), copyTscCommandData(e.getValue()));
			tscListModel.addElement(e.getKey());
		}
		musicListModel.clear();
		for (String s : musicListSnapshot) musicListModel.addElement(s);
		sfxListModel.clear();
		for (String s : sfxListSnapshot) sfxListModel.addElement(s);
		requiresRestart = false;
	}

	private static class TscCommandData {
		String name;
		int ops;
		String[] ids;
		String abb;
		String desc;
	}
}
