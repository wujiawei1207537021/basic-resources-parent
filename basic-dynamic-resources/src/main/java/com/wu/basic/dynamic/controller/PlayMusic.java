package com.wu.basic.dynamic.controller;

import com.sun.media.sound.JavaSoundAudioClip;
import sun.applet.AppletAudioClip;

import java.applet.AudioClip;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * @author : 吴佳伟
 * @version 1.0
 * @describe :
 * @date : 2021/2/28 9:44 下午
 */






public class PlayMusic {


    public static void main(String[] args) throws Exception {
        InputStream inputStream=new FileInputStream("/Users/wujiawei/Desktop/aa.mp3");
        final JavaSoundAudioClip javaSoundAudioClip = new JavaSoundAudioClip(inputStream);
        javaSoundAudioClip.play();
        AudioClip appletAudioClip = new AppletAudioClip(new URL("file:/Users/wujiawei/Desktop/aa.mp3"));
        appletAudioClip.loop();
    }

}