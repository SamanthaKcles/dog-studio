package ca.noxid.lab.entity;

import ca.noxid.lab.mapdata.MapInfo.PxeEntry;
import ca.noxid.lab.rsrc.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class EntitySettingsPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 4649521655943734465L;

    private JCheckBox[] flagArray;

    private Set<PxeEntry> entityList;

    private static final int[] FLAG_ORDER = {
         5, // Shootable
         1, // Not Affected By Tile44
         3, // Ignore Solid
        10, // (Unused)
         0, // Solid (bouncy sides)
         6, // Solid (hard sides)
         4, // Bouncy Top
         7, // Rear and top no damage
        11, // Appear once flagID set
        14, // No Appear if flagID set
         9, // Run event on death
         8, // Run event on contact
        13, // Interactable
        12, // Spawn with alt direction
        15, // Show Damage #
         2  // Invulnerable
    };

    private static final int COLUMNS = 4;

    public EntitySettingsPanel(ResourceManager iMan) {
        super(new GridBagLayout());
        setOpaque(false);

        flagArray = new JCheckBox[16];
        for (int i = 0; i < flagArray.length; i++) {
            flagArray[i] = new JCheckBox(EntityData.flagNames[i]);
            flagArray[i].addActionListener(this);
            flagArray[i].setOpaque(false);
        }

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(1, 2, 1, 4);
        c.fill = GridBagConstraints.NONE;

        for (int slot = 0; slot < FLAG_ORDER.length; slot++) {
            c.gridx = slot % COLUMNS;
            c.gridy = slot / COLUMNS;
            this.add(flagArray[FLAG_ORDER[slot]], c);
        }

        setAllEnabled(false);
    }

    public void rebind(Set<PxeEntry> newList) {
        entityList = newList;
        listChanged();
    }

    public void unbind() {
        entityList = null;
        setAllEnabled(false);
        for (JCheckBox cb : flagArray) cb.setSelected(false);
    }

    private void setAllEnabled(boolean state) {
        for (JCheckBox cb : flagArray) {
            cb.setEnabled(state);
        }
    }

    public void listChanged() {
        if (entityList == null || entityList.isEmpty()) {
            setAllEnabled(false);
            for (JCheckBox cb : flagArray) cb.setSelected(false);
        } else if (entityList.size() == 1) {
            int flags = entityList.iterator().next().getFlags();
            for (int i = 0; i < flagArray.length; i++) {
                flagArray[i].setSelected((flags & (1 << i)) != 0);
                flagArray[i].setText(EntityData.flagNames[i]);
                flagArray[i].setEnabled(true);
            }
        } else {
            int[] flagsCount = new int[flagArray.length];
            for (PxeEntry p : entityList) {
                for (int i = 0; i < flagArray.length; i++) {
                    if ((p.getFlags() & (1 << i)) != 0) flagsCount[i]++;
                }
            }
            for (int i = 0; i < flagArray.length; i++) {
                if (flagsCount[i] == 0) {
                    flagArray[i].setSelected(false);
                    flagArray[i].setText(EntityData.flagNames[i]);
                } else {
                    flagArray[i].setSelected(true);
                    flagArray[i].setText(EntityData.flagNames[i] + "*" + flagsCount[i]);
                }
                flagArray[i].setEnabled(true);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent eve) {
        if (entityList == null || entityList.isEmpty()) return;
        for (int i = 0; i < flagArray.length; i++) {
            if (eve.getSource() == flagArray[i]) {
                int selected = flagArray[i].isSelected() ? 1 : 0;
                for (PxeEntry e : entityList) {
                    int f = e.getFlags();
                    f &= ~(1 << i);
                    f |= selected << i;
                    e.setFlags(f);
                }
                listChanged();
                break;
            }
        }
    }
}
