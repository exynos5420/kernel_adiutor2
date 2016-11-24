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

package com.exynos5420.deathlyadiutor.fragments.kernel;

import android.os.Bundle;
import android.text.InputType;

import com.exynos5420.deathlyadiutor.R;
import com.exynos5420.deathlyadiutor.elements.DDivider;
import com.exynos5420.deathlyadiutor.elements.cards.EditTextCardView;
import com.exynos5420.deathlyadiutor.elements.cards.SeekBarCardView;
import com.exynos5420.deathlyadiutor.elements.cards.SwitchCardView;
import com.exynos5420.deathlyadiutor.fragments.RecyclerViewFragment;
import com.exynos5420.deathlyadiutor.utils.kernel.VM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 27.12.14.
 */

public class VMFragment extends RecyclerViewFragment implements SeekBarCardView.DSeekBarCard.OnDSeekBarCardListener, SwitchCardView.DSwitchCard.OnDSwitchCardListener {

    private EditTextCardView.DEditTextCard mMinFreeKbytesCard;

    private SeekBarCardView.DSeekBarCard mDirtyRatioCard, mDirtyBackgroundRatioCard, mDirtyExpireCard, mDirtyWritebackCard, mOverCommitRatioCard, mSwappinessCard, mVFSCachePressureCard, mZRAMDisksizeCard;

    private SwitchCardView.DSwitchCard mLaptopModeCard;

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        dirtyratioInit();
        dirtybackgroundratioInit();
        dirtyexpireInit();
        dirtywritebackInit();
        overcommitratioInit();
        swappinessInit();
        vfscachepressureInit();

        laptopmodeInit();

        minfreekbytesInit();
    }

    private void dirtyratioInit() {
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.disabled));
        for (int i = 1; i <= 100; i++)
            list.add(i + getString(R.string.percent));

        mDirtyRatioCard = new SeekBarCardView.DSeekBarCard(list);
        mDirtyRatioCard.setTitle(getString(R.string.dirty_ratio));
        mDirtyRatioCard.setDescription(getString(R.string.dirty_ratio_summary));
        mDirtyRatioCard.setProgress(VM.getDirtyRatio());
        mDirtyRatioCard.setOnDSeekBarCardListener(this);

        addView(mDirtyRatioCard);
    }

    private void dirtybackgroundratioInit() {
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.disabled));
        for (int i = 1; i <= 100; i++)
            list.add(i + getString(R.string.percent));

        mDirtyBackgroundRatioCard = new SeekBarCardView.DSeekBarCard(list);
        mDirtyBackgroundRatioCard.setTitle(getString(R.string.dirty_background_ratio));
        mDirtyBackgroundRatioCard.setDescription(getString(R.string.dirty_background_ratio_summary));
        mDirtyBackgroundRatioCard.setProgress(VM.getDirtyBackgroundRatio());
        mDirtyBackgroundRatioCard.setOnDSeekBarCardListener(this);

        addView(mDirtyBackgroundRatioCard);
    }

    private void dirtyexpireInit() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 500; i++)
            list.add(i * 10 + getString(R.string.cs));

        mDirtyExpireCard = new SeekBarCardView.DSeekBarCard(list);
        mDirtyExpireCard.setTitle(getString(R.string.dirty_expire_centisecs));
        mDirtyExpireCard.setDescription(getString(R.string.dirty_expire_centisecs_summary));
        mDirtyExpireCard.setProgress((VM.getDirtyExpire() / 10) - 1);
        mDirtyExpireCard.setOnDSeekBarCardListener(this);

        addView(mDirtyExpireCard);
    }

    private void dirtywritebackInit() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 900; i++)
            list.add(i * 10 + getString(R.string.cs));

        mDirtyWritebackCard = new SeekBarCardView.DSeekBarCard(list);
        mDirtyWritebackCard.setTitle(getString(R.string.dirty_writeback_centisecs));
        mDirtyWritebackCard.setDescription(getString(R.string.dirty_writeback_centisecs_summary));
        mDirtyWritebackCard.setProgress((VM.getDirtyWriteback()) - 1);
        mDirtyWritebackCard.setOnDSeekBarCardListener(this);

        addView(mDirtyWritebackCard);
    }

    private void overcommitratioInit() {
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.disabled));
        for (int i = 1; i <= 100; i++)
            list.add(i + getString(R.string.percent));

        mOverCommitRatioCard = new SeekBarCardView.DSeekBarCard(list);
        mOverCommitRatioCard.setTitle(getString(R.string.overcommit_ratio));
        mOverCommitRatioCard.setDescription(getString(R.string.overcommit_ratio_summary));
        mOverCommitRatioCard.setProgress(VM.getOverCommitRatio());
        mOverCommitRatioCard.setOnDSeekBarCardListener(this);

        addView(mOverCommitRatioCard);

    }

    private void swappinessInit() {
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.disabled));
        for (int i = 1; i <= 100; i++)
            list.add(i + getString(R.string.percent));

        mSwappinessCard = new SeekBarCardView.DSeekBarCard(list);
        mSwappinessCard.setTitle(getString(R.string.swappiness));
        mSwappinessCard.setDescription(getString(R.string.swappiness_summary));
        mSwappinessCard.setProgress(VM.getSwappiness());
        mSwappinessCard.setOnDSeekBarCardListener(this);

        addView(mSwappinessCard);
    }

    private void vfscachepressureInit() {
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.disabled));
        for (int i = 1; i <= 150; i++)
            list.add(String.valueOf(i));

        mVFSCachePressureCard = new SeekBarCardView.DSeekBarCard(list);
        mVFSCachePressureCard.setTitle(getString(R.string.vfs_cache_pressure));
        mVFSCachePressureCard.setDescription(getString(R.string.vfs_cache_pressure_summary));
        mVFSCachePressureCard.setProgress(VM.getVFSCachePressure() - 1);
        mVFSCachePressureCard.setOnDSeekBarCardListener(this);

        addView(mVFSCachePressureCard);
    }

    private void laptopmodeInit() {
        mLaptopModeCard = new SwitchCardView.DSwitchCard();
        mLaptopModeCard.setTitle(getString(R.string.laptop_mode));
        mLaptopModeCard.setDescription(getString(R.string.laptop_mode_summary));
        mLaptopModeCard.setChecked(VM.isLaptopModeActive());
        mLaptopModeCard.setOnDSwitchCardListener(this);

        addView(mLaptopModeCard);
    }

    private void minfreekbytesInit() {
        DDivider mMinFreeKbytesDividerCard = new DDivider();
        mMinFreeKbytesDividerCard.setText(getString(R.string.min_free_kbytes));
        mMinFreeKbytesDividerCard.setDescription(getString(R.string.min_free_kbytes_summary));
        addView(mMinFreeKbytesDividerCard);

        String value = VM.getMinFreeKbytes();
        mMinFreeKbytesCard = new EditTextCardView.DEditTextCard();
        mMinFreeKbytesCard.setDescription(value + " kb");
        mMinFreeKbytesCard.setValue(value);
        mMinFreeKbytesCard.setInputType(InputType.TYPE_CLASS_NUMBER);
        mMinFreeKbytesCard.setOnDEditTextCardListener(new EditTextCardView.DEditTextCard.OnDEditTextCardListener() {
            @Override
            public void onApply(EditTextCardView.DEditTextCard dEditTextCard, String value) {
                VM.setMinFreeKbytes(value.replace(" kb", ""), getActivity());
                dEditTextCard.setDescription(value + " kb");
            }
        });

        addView(mMinFreeKbytesCard);

    }

    @Override
    public void onChanged(SeekBarCardView.DSeekBarCard dSeekBarCard, int position) {
    }

    @Override
    public void onStop(SeekBarCardView.DSeekBarCard dSeekBarCard, int position) {
        if (dSeekBarCard == mDirtyRatioCard) VM.setDirtyRatio(position, getActivity());
        else if (dSeekBarCard == mDirtyBackgroundRatioCard)
            VM.setDirtyBackgroundRatio(position, getActivity());
        else if (dSeekBarCard == mDirtyExpireCard)
            VM.setDirtyExpire((position + 1) * 10, getActivity());
        else if (dSeekBarCard == mDirtyWritebackCard)
            VM.setDirtyWriteback(position + 1, getActivity());
        else if (dSeekBarCard == mOverCommitRatioCard)
            VM.setOverCommitRatio(position, getActivity());
        else if (dSeekBarCard == mSwappinessCard) VM.setSwappiness(position, getActivity());
        else if (dSeekBarCard == mVFSCachePressureCard)
            VM.setVFSCachePressure(position + 1, getActivity());
    }

    @Override
    public void onChecked(SwitchCardView.DSwitchCard dSwitchCard, boolean checked) {
        if (dSwitchCard == mLaptopModeCard)
            VM.activateLaptopMode(checked, getActivity());
    }
}