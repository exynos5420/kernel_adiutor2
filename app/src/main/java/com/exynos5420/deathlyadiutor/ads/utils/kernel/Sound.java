/*
 * Copyright (C) 2016 Martin Bouchet
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

package com.exynos5420.deathlyadiutor.ads.utils.kernel;

import android.content.Context;

import com.exynos5420.deathlyadiutor.ads.utils.Constants;
import com.exynos5420.deathlyadiutor.ads.utils.Utils;
import com.exynos5420.deathlyadiutor.ads.utils.root.Control;

/**
 * Created by Martin Bouchet on 21.11.16.
 */
public class Sound implements Constants {

    public static boolean hasWolfsonChip(){
        return Utils.existFile(SP_GAIN_LEFT);
    }

    //Speaker
    public static boolean hasEarpiece(){
        return Utils.existFile(EARPICE_VOLUME);
    }

    public static String getEarpieceVolume(){
        double value = Utils.stringtodouble(Utils.readFile(EARPICE_VOLUME));
            return Double.toString((value + 8) * 2);
    }

    public static void setEarpieceVolume(int volume, Context context){
        double volumewithdec = volume;
        volumewithdec = (volumewithdec/2) - 8;
            Control.runCommand(Double.toString(volumewithdec), EARPICE_VOLUME, Control.CommandType.GENERIC, context);
    }

    public static String getSpeakerGainLeft(){
        double value = Utils.stringtodouble(Utils.readFile(SP_GAIN_LEFT));
        return Double.toString((value + 8) * 2);
    }

    public static void setSpeakerGainLeft(int gain, Context context){
        double gainwithdec = gain;
        gainwithdec = (gainwithdec/2) - 8;
        Control.runCommand(Double.toString(gainwithdec), SP_GAIN_LEFT, Control.CommandType.GENERIC, context);
    }

    public static String getSpeakerGainRight(){
        double value = Utils.stringtodouble(Utils.readFile(SP_GAIN_RIGHT));
        return Double.toString((value + 8) * 2);
    }

    public static void setSpeakerGainRight(int gain, Context context){
        double gainwithdec = gain;
        gainwithdec = (gainwithdec/2) - 8;
        Control.runCommand(Double.toString(gainwithdec), SP_GAIN_RIGHT, Control.CommandType.GENERIC, context);
    }

    public static boolean isSpeakerEqEnabled(){
        return Utils.readFile(SP_EQ_ENABLE).equals("1");
    }

    public static void switchSpeakerEq(boolean activate, Context context){
        Control.runCommand(activate ? "1" : "0", SP_EQ_ENABLE, Control.CommandType.GENERIC, context);
    }

    public static boolean isSpeakerPrivacyEnabled(){
        return Utils.readFile(SP_PRIVACY_MODE).equals("1");
    }

    public static void switchSpeakerPrivacy(boolean activate, Context context){
        Control.runCommand(activate ? "1" : "0", SP_PRIVACY_MODE, Control.CommandType.GENERIC, context);
    }

    public static String getSpeakerEqGain(int band){
        double value = Utils.stringtodouble(Utils.readFile(SPEAKER_EQ_GAINS_ARRAY[band]));
        return Double.toString((value + 12) * 2);
    }

    public static void setSpeakerEqGain(int gain, int band, Context context){
        double gainwithdec = gain;
        gainwithdec = (gainwithdec/2) - 12;
        Control.runCommand(Double.toString(gainwithdec), SPEAKER_EQ_GAINS_ARRAY[band], Control.CommandType.GENERIC, context);
    }

    //Headphone
    public static String getHeadphoneGainLeft(){
        double value = Utils.stringtodouble(Utils.readFile(HP_GAIN_LEFT));
        return Double.toString((value - 80) * 2);
    }

    public static void setHeadphoneGainLeft(int gain, Context context){
        double gainwithdec = gain;
        gainwithdec = (gainwithdec/2) + 80;
        Control.runCommand(Double.toString(gainwithdec), HP_GAIN_LEFT, Control.CommandType.GENERIC, context);
    }

    public static String getHeadphoneGainRight(){
        double value = Utils.stringtodouble(Utils.readFile(HP_GAIN_RIGHT));
        return Double.toString((value - 80) * 2);
    }

    public static void setHeadphoneGainRight(int gain, Context context){
        double gainwithdec = gain;
        gainwithdec = (gainwithdec/2) + 80;
        Control.runCommand(Double.toString(gainwithdec), HP_GAIN_RIGHT, Control.CommandType.GENERIC, context);
    }

    public static boolean isHeadphoneMono(){
        return Utils.readFile(HP_SWITCH_MONO).equals("1");
    }

    public static void switchHeadphonMono(boolean activate, Context context){
        Control.runCommand(activate ? "1" : "0", HP_SWITCH_MONO, Control.CommandType.GENERIC, context);
    }

    public static boolean isHeadphoneEqEnabled(){
        return Utils.readFile(HP_EQ_ENABLE).equals("1");
    }

    public static void switchHeadphoneEq(boolean activate, Context context){
        Control.runCommand(activate ? "1" : "0", HP_EQ_ENABLE, Control.CommandType.GENERIC, context);
    }

    public static String getHeadphoneEqGain(int band){
        double value = Utils.stringtodouble(Utils.readFile(HEADPHONE_EQ_GAINS_ARRAY[band]));
        return Double.toString((value + 12) * 2);
    }

    public static void setHeadphoneEqGain(int gain, int band, Context context){
        double gainwithdec = gain;
        gainwithdec = (gainwithdec/2) - 12;
        Control.runCommand(Double.toString(gainwithdec), HEADPHONE_EQ_GAINS_ARRAY[band], Control.CommandType.GENERIC, context);
    }
}
