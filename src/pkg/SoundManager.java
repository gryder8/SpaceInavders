package pkg;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

class SoundManager {

    private static File invaderKilled = new File("src/assets/invaderKilled.wav");
    private static File shoot = new File("src/assets/shoot.wav");
    private static File playerDeath = new File("src/assets/explosion.wav");
    private static File mysteryShipIntro = new File("src/assets/ufo-lowpitch.wav");
    private static Clip clip;
    private static AudioInputStream audioIn;

    @SuppressWarnings("Duplicates")
    static void shootSound() {
        try {
            audioIn = AudioSystem.getAudioInputStream(shoot);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException e){
            System.err.println("Error reading file. Unsupported file type!");
        } catch (IOException e){
            System.err.println("An error occurred trying to play the audio file");
        } catch (LineUnavailableException e){
            System.err.println("No line available for the current audio clip");
        }
    }

    @SuppressWarnings("Duplicates")
    static void invaderKilledSound (){
        try {
            audioIn = AudioSystem.getAudioInputStream(invaderKilled);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException e){
            System.err.println("Error reading file. Unsupported file type!");
        } catch (IOException e){
            System.err.println("An error occurred trying to play the audio file");
        } catch (LineUnavailableException e){
            System.err.println("No line available for the current audio clip");
        }
    }

    @SuppressWarnings("Duplicates")
    static void playerDeathSound(){
        try {
            audioIn = AudioSystem.getAudioInputStream(playerDeath);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException e){
            System.err.println("Error reading file. Unsupported file type!");
        } catch (IOException e){
            System.err.println("An error occurred trying to play the audio file");
        } catch (LineUnavailableException e){
            System.err.println("No line available for the current audio clip");
        }
    }

    static void mysteryShipIntroSound(){
        try {
            audioIn = AudioSystem.getAudioInputStream(mysteryShipIntro);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException e){
            System.err.println("Error reading file. Unsupported file type!");
        } catch (IOException e){
            System.err.println("An error occurred trying to play the audio file");
        } catch (LineUnavailableException e){
            System.err.println("No line available for the current audio clip");
        }
    }



}
