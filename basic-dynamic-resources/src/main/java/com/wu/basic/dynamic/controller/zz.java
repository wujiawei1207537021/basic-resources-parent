package com.wu.basic.dynamic.controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author : 吴佳伟
 * @version 1.0
 * @describe :
 * @date : 2021/2/28 9:35 下午
 */
public class zz {



        public static void main(String[] args) {
            PlayTest playTest = new PlayTest("/Users/wujiawei/Desktop/aa.mp3");
            playTest.start();
            System.out.println("dd");
//        playTest.stop();
        }


    static class PlayTest extends Thread {
        Player player;
        String music;

        public PlayTest(String file) {
            this.music = file;

        }

        @Override
        public void run() {
            try {
                play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void play() throws FileNotFoundException, JavaLayerException {
            BufferedInputStream buffer =
                    new BufferedInputStream(
                            new FileInputStream(music));
            player = new Player(buffer);
            player.play();
        }
    }

}
