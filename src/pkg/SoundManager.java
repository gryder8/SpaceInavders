package pkg;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

class SoundManager {

    private static File invaderKilled = new File("src/invaderKilled.wav");
    private static File shoot = new File("src/shoot.wav");
    private static File playerDeath = new File("src/explosion.wav");
    private static Clip clip;
    private static AudioInputStream audioIn;

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



}
