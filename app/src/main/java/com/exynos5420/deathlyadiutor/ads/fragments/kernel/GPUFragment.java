/*
 * Copyright (C) 2016 Martin Bouchet
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

package com.exynos5420.deathlyadiutor.ads.fragments.kernel;

import android.os.Bundle;

import com.exynos5420.deathlyadiutor.ads.R;
import com.exynos5420.deathlyadiutor.ads.elements.cards.CardViewItem;
import com.exynos5420.deathlyadiutor.ads.elements.cards.PopupCardView;
import com.exynos5420.deathlyadiutor.ads.elements.cards.UsageCardView;
import com.exynos5420.deathlyadiutor.ads.fragments.RecyclerViewFragment;
import com.exynos5420.deathlyadiutor.ads.utils.kernel.GPU;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 26.12.14.
 */
public class GPUFragment extends RecyclerViewFragment implements PopupCardView.DPopupCard.OnDPopupCardListener {

    private CardViewItem.DCardView mCurFreqCard, mVoltNowCard;

    private PopupCardView.DPopupCard mMaxFreqCard;

    private PopupCardView.DPopupCard mMinFreqCard;

    private PopupCardView.DPopupCard mGovernorCard, mPowerPolicyCard;

    private UsageCardView.DUsageCard mUsageCard;

    private int offset = 0;

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        if (GPU.getGpuUsage() != -1) usageInit();
        if (GPU.getGpuCurFreq() != -1) curFreqInit();
        if (GPU.getGpuVoltageNow() != -1) VoltNowInit();
        if (GPU.getGpuMaxFreq() != -1) maxFreqInit();
        if (GPU.getGpuMinFreq() != -1) minFreqInit();
        if (!GPU.getGpuPowerPolicy().equals("-1"))powerpolicyInit();
        if (!GPU.getGpuGovernor().equals("-1")) governorInit();

    }
    private void usageInit() {
        mUsageCard = new UsageCardView.DUsageCard();
        mUsageCard.setText(getString(R.string.gpu_usage));

        mUsageCard.setProgress(GPU.getGpuUsage());

        addView(mUsageCard);
    }

    private void curFreqInit() {

        mCurFreqCard = new CardViewItem.DCardView();
        mCurFreqCard.setTitle(getString(R.string.gpu_cur_freq));

        addView(mCurFreqCard);
    }

    private void VoltNowInit() {

        mVoltNowCard = new CardViewItem.DCardView();
        mVoltNowCard.setTitle(getString(R.string.gpu_voltage_now));

        addView(mVoltNowCard);
    }

    private void maxFreqInit() {

        List<String> freqs = new ArrayList<>();
        for (int i = 0; i < GPU.getGpuFreqs().size(); i++) {
            freqs.add(GPU.getGpuFreqs().get(i) + getString(R.string.mhz));
        }

        mMaxFreqCard = new PopupCardView.DPopupCard(freqs);
        mMaxFreqCard.setTitle(getString(R.string.gpu_max_freq));
        mMaxFreqCard.setDescription(getString(R.string.gpu_max_freq_summary));
        mMaxFreqCard.setItem(GPU.getGpuMaxFreq() + getString(R.string.mhz));
        mMaxFreqCard.setOnDPopupCardListener(this);

        addView(mMaxFreqCard);
    }

    private void minFreqInit() {
        List<String> freqs = new ArrayList<>();
        for (int i = 0; i < GPU.getGpuFreqs().size(); i++) {
            freqs.add(GPU.getGpuFreqs().get(i) + getString(R.string.mhz));
        }

        mMinFreqCard = new PopupCardView.DPopupCard(freqs);
        mMinFreqCard.setTitle(getString(R.string.gpu_min_freq));
        mMinFreqCard.setDescription(getString(R.string.gpu_min_freq_summary));
        mMinFreqCard.setItem(GPU.getGpuMinFreq() + getString(R.string.mhz));
        mMinFreqCard.setOnDPopupCardListener(this);

        addView(mMinFreqCard);
    }

    private void powerpolicyInit() {

        mPowerPolicyCard = new PopupCardView.DPopupCard(GPU.getGpuPowerPolicies());
        mPowerPolicyCard.setTitle(getString(R.string.gpu_powerpolicy));
        mPowerPolicyCard.setDescription(getString(R.string.gpu_powerpolicy_summary));
        mPowerPolicyCard.setItem(GPU.getGpuPowerPolicy());
        mPowerPolicyCard.setOnDPopupCardListener(this);

        addView(mPowerPolicyCard);
    }

    private void governorInit() {

        mGovernorCard = new PopupCardView.DPopupCard(GPU.getGpuAvailibleGovernors());
        mGovernorCard.setTitle(getString(R.string.gpu_governor));
        mGovernorCard.setDescription(getString(R.string.gpu_governor_summary));
        mGovernorCard.setItem(GPU.getGpuGovernor());
        mGovernorCard.setOnDPopupCardListener(this);

        addView(mGovernorCard);
    }

    @Override
    public void onItemSelected(PopupCardView.DPopupCard dPopupCard, int position) {
        if (dPopupCard == mMaxFreqCard)
            GPU.setGpuMaxFreq(GPU.getGpuFreqs().get(position), getActivity());
        else if (dPopupCard == mMinFreqCard)
            GPU.setGpuMinFreq(GPU.getGpuFreqs().get(position), getActivity());
        else if (dPopupCard == mPowerPolicyCard)
            GPU.setGpuPowerPolicy(GPU.getGpuPowerPolicies().get(position), getActivity());
        else if (dPopupCard == mGovernorCard)
            GPU.setGpuGovernor(GPU.getGpuAvailibleGovernors().get(position), getActivity());

    }

    @Override
    public boolean onRefresh() {

        if (mCurFreqCard != null)
            mCurFreqCard.setDescription((GPU.getGpuCurFreq()) + getString(R.string.mhz));

        if (mVoltNowCard != null)
            mVoltNowCard.setDescription((GPU.getGpuVoltageNow()) + getString(R.string.mv));

        if (mUsageCard != null)
            mUsageCard.setProgress(GPU.getGpuUsage());

        return true;
    }

}