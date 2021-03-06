/*
 * Copyright (C) 2015 Willi Ye
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exynos5420.deathlyadiutor.ads.fragments.tools;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.exynos5420.deathlyadiutor.ads.R;
import com.exynos5420.deathlyadiutor.ads.elements.DDivider;
import com.exynos5420.deathlyadiutor.ads.elements.cards.CardViewItem;
import com.exynos5420.deathlyadiutor.ads.fragments.RecyclerViewFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.BatteryFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.CPUFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.CPUVoltageFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.EntropyFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.GPUFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.GPUVoltageFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.GPUThermalFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.IOFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.KSMFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.LMKFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.MiscFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.ScreenFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.SpeakerFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.HeadphoneFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.CPUThermalFragment;
import com.exynos5420.deathlyadiutor.ads.fragments.kernel.VMFragment;
import com.exynos5420.deathlyadiutor.ads.utils.Utils;
import com.exynos5420.deathlyadiutor.ads.utils.database.CommandDB;
import com.exynos5420.deathlyadiutor.ads.utils.root.Control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by willi on 25.04.15.
 */
public class StartUpCommandsFragment extends RecyclerViewFragment {

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    Control.deletespecificcommand(getActivity(), null, null);
                    forcerefresh(getActivity());
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
    private CardViewItem.DCardView mAllStartUpCommandsCard;
    private CardViewItem.DCardView[] mStartUpCommands;

    @Override
    public boolean showApplyOnBoot() {
        return false;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        listcommands();
    }

    public void listcommands() {
        CommandDB commandDB = new CommandDB(getActivity());
        List<CommandDB.CommandItem> commandItems = commandDB.getAllCommands();
        final List<String> applys = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        Class[] classes = {BatteryFragment.class, CPUFragment.class,
                CPUVoltageFragment.class, GPUVoltageFragment.class, GPUThermalFragment.class, EntropyFragment.class, GPUFragment.class, IOFragment.class,
                KSMFragment.class, LMKFragment.class, MiscFragment.class, ScreenFragment.class, SpeakerFragment.class, HeadphoneFragment.class,
                CPUThermalFragment.class, VMFragment.class
        };

        for (Class mClass : classes)
            if (Utils.getBoolean(mClass.getSimpleName() + "onboot", false, getContext())) {
                applys.addAll(Utils.getApplys(mClass));
            }

        if (applys.size() > 0)
            for (CommandDB.CommandItem commandItem : commandItems)
                for (String sys : applys) {
                    String path = commandItem.getPath();
                    if ((sys.contains(path) || path.contains(sys))) {
                        String command = commandItem.getCommand();
                        if (commands.indexOf(command) < 0)
                            commands.add(command);
                    }
                }

        if (commands.size() > 0) {
            mAllStartUpCommandsCard = new CardViewItem.DCardView();
            mAllStartUpCommandsCard.setTitle(getString(R.string.all_startup_commands));
            final String allcommands = android.text.TextUtils.join("\n", commands);

            mAllStartUpCommandsCard.setOnDCardListener(new CardViewItem.DCardView.OnDCardListener() {
                @Override
                public void onClick(CardViewItem.DCardView dCardView) {
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getActivity()).setItems(getResources().getStringArray(R.array.startup_commands_menu),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case 0: {
                                                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                                    ClipData clip = ClipData.newPlainText("Startup Comnmands Shell", allcommands);
                                                    clipboard.setPrimaryClip(clip);
                                                    break;
                                                }
                                                case 1: {
                                                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                                    ClipData clip = ClipData.newPlainText("Startup Comnmands RC", convert_to_rc(allcommands));
                                                    clipboard.setPrimaryClip(clip);
                                                    break;
                                                }
                                                case 2: {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                                    builder.setMessage(getString(R.string.startup_commands_delete_all)).setPositiveButton(getString(R.string.startup_commands_delete_all_yes), dialogClickListener)
                                                            .setNegativeButton(getString(R.string.startup_commands_delete_all_no), dialogClickListener).show();
                                                    break;
                                                }

                                            }
                                        }
                                    }).show();
                        }
                    });
                }
            });
            addView(mAllStartUpCommandsCard);

            DDivider mStartUpCommandsListDividerCard = new DDivider();
            mStartUpCommandsListDividerCard.setText(getString(R.string.startup_commands_list));
            mStartUpCommandsListDividerCard.setDescription(getString(R.string.startup_commands_list_info));
            addView(mStartUpCommandsListDividerCard);

            mStartUpCommands = new CardViewItem.DCardView[commandItems.size()];
            for (int i = 0; i < commands.size(); i++) {
                mStartUpCommands[i] = new CardViewItem.DCardView();
                mStartUpCommands[i].setDescription(commands.get(i));
                final String command = commands.get(i);
                mStartUpCommands[i].setOnDCardListener(new CardViewItem.DCardView.OnDCardListener() {
                    @Override
                    public void onClick(CardViewItem.DCardView dCardView) {

                        new AlertDialog.Builder(getActivity()).setItems(getResources().getStringArray(R.array.startup_commands_menu),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0: {
                                                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                                ClipData clip = ClipData.newPlainText("Startup Comnmand", command);
                                                clipboard.setPrimaryClip(clip);
                                                break;
                                            }
                                            case 1: {
                                                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                                ClipData clip = ClipData.newPlainText("Startup Comnmands RC", convert_to_rc(command));
                                                clipboard.setPrimaryClip(clip);
                                                break;
                                            }
                                            case 2: {
                                                Control.deletespecificcommand(getActivity(), null, command);
                                                forcerefresh(getActivity());
                                                break;
                                            }

                                        }
                                    }
                                }).show();

                    }
                });
                addView(mStartUpCommands[i]);
            }
        } else {
            mAllStartUpCommandsCard = new CardViewItem.DCardView();
            mAllStartUpCommandsCard.setTitle(getString(R.string.startup_commands_none));

            addView(mAllStartUpCommandsCard);
        }
    }

    public void forcerefresh(Context context) {
        view.invalidate();
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    private String convert_to_rc(String text) {
        StringBuilder sb = new StringBuilder();
        BufferedReader bufReader = new BufferedReader(new StringReader(text));
        try {
            String line = null;
            while ((line = bufReader.readLine()) != null) {
                String[] tar = line.split(" ");
                if (tar[0].equals("echo")) {
                    String fparse[] = line.split(">");
                    String fcmd = fparse[0].replaceFirst("^echo", "");
                    sb.append("write" + fparse[1] + fcmd + "\n");
                } else {
                    sb.append(line + "\n");
                }
            }
        } catch (IOException x) {
        }
        return sb.toString();
    }

}
