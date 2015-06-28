/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package template;

/**
 *
 * @author tengfei
 */

import de.affect.xml.EmotionType;
import de.affect.manage.AffectManager;
import de.affect.manage.event.AffectUpdateEvent;
import de.affect.manage.event.AffectUpdateListener;
import de.affect.util.AppraisalTag;
import de.affect.xml.AffectInputDocument.AffectInput;
import de.affect.xml.AffectOutputDocument;
import de.affect.xml.AffectOutputDocument.AffectOutput.CharacterAffect;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.xmlbeans.XmlException;

/**
 * <code>AffectEngine</code> shows a typical way using ALMA. The example class
 * provides a method for handling affect input and handles the computed affect.
 */
public class AlmaMonitor implements AffectUpdateListener {
  // The ALMA Java implementation

  public static AffectManager fAM = null;
  // ALMA configuration files
  //private static String sALMACOMP = "conf/AffectComputation.aml";
  //private static String sALMADEF = "conf/CharacterDefinition.aml";
  private static String sALMACOMP = "conf/AffectComputationExample.aml";
  private static String sALMADEF = "conf/AffectDefinitionExample.aml";
  // ALMA mode: 
  //     false - output on console 
  //     true - graphical user interface CharacterBuilder
  //            NOTE: No runtime windows (defined in AffectComputation or
  //                  AffectDefinition will be displayed!)
  private static final boolean sGUIMode = true;
  // Console logging 
  public static Logger log = Logger.getLogger("Alma");

  public AlmaMonitor() {
    // Starting the ALMA affect engine

    try {

      fAM = new AffectManager(sALMACOMP, sALMADEF, sGUIMode);
      fAM.addAffectUpdateListener(this);
    } catch (IOException io) {
      log.info("Error during ALMA initialisation");
      io.printStackTrace();
      System.exit(-1);
    } catch (XmlException xmle) {
      log.info("Error in ALMA configuration");
      xmle.printStackTrace();
      System.exit(-1);
    }
  }

  /**
   * Listens to affect updates computed by ALMA. This implements the
   * AffectUpdateListener
   */
  public synchronized void update(AffectUpdateEvent event) {
    AffectOutputDocument aod = event.getUpdate();
    log.info(">>>");
    try {
      for (Iterator<CharacterAffect> it = aod.getAffectOutput().getCharacterAffectList().iterator(); it.hasNext();) {
        CharacterAffect character = it.next();

        // access cached data or create new cache
        String name = character.getName();
        String emotion = character.getDominantEmotion().getName().toString();
        double eIntensity = Double.parseDouble(character.getDominantEmotion().getValue());
        String mood = character.getMood().getMoodword().toString();
        String mIntensity = character.getMood().getIntensity().toString();
        String mTendency = character.getMoodTendency().getMoodword().toString();

        //TODO use affect for something!
        log.info(name + " has dominant emotion " + emotion + "(" + eIntensity + ")");

        // get the intensity of all active emotions of the character
        for (Iterator<EmotionType> emo = character.getEmotions().getEmotionList().iterator(); emo.hasNext();) {
          EmotionType et = emo.next();
          log.info(name + "'s " + et.getName().toString() + " has intensity " + et.getValue());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    log.info("<<<");
  }

  /**
   * The
   * <code>processInput</code> creates a new instance of AffectInput and passes
   * it to the AffectManager
   *
   * See AppraisalTag.java in de.affect.util for the generation affect input!
   */
  public void processInput(String character, String input, String intensity, String elicitor) {
    AffectInput ai = AppraisalTag.instance().makeAffectInput(character, input, intensity, elicitor);

    //debug    log.info(ai.toString());

    fAM.processSignal(ai);
  }

  /**
   * The
   * <code>processInput</code> creates a new instance of PAD - AffectInput and
   * passes it to the AffectManager
   *
   * See AppraisalTag.java in de.affect.util for the generation affect input!
   */
  public void processInput(String character, String p, String a, String d, String intensity, String elicitor) {
    AffectInput ai = AppraisalTag.instance().makePADInput(character, p, a, d, intensity, elicitor);

    //debug   log.info(ai.toString());
    fAM.processSignal(ai);
  }

  /**
   * Wrapper method for the call for pausing the affect computation
   */
  public void pauseAffectComputation() {
    fAM.sInterface.pauseAffectComputation();

    // It is also possible to pause the affect computation of a single character /group
    // fAM.sInterface.pauseAffectComputation("Anne");
  }

  /**
   * Wrapper method for the call for resuming a paused affect computation
   */
  public void resumeAffectComputation() {
    fAM.sInterface.resumeAffectComputation();

    // It is also possible to resume a paused  affect computation of a single character /group
    // fAM.sInterface.resumeAffectComputation("Anne");
  }

  /**
   * Wrapper method for the call for a stepwise execution of a paused affect
   * computation
   */
  public void stepwiseAffectComputation() {
    fAM.sInterface.stepwiseAffectComputation();

    // It is also possible to stepwise execute a pause affect computation of a single character /group
    // fAM.sInterface.stepwiseAffectComputation("Anne");
  }

  /**
   * A simple main method.
   */
 /* public static void main(String[] args) {
    log.info("Starting ALMA 2.0 example");


    // make the engine
    AlmaMonitor mAE = new  AlmaMonitor();

    // wait 2 seconds
    try {
      Thread.currentThread().sleep(2000);
    } catch (InterruptedException ie) {
      log.warning("Sleep interrupted!");
    }

    log.info("Trigger some emotions");

    // input!
    mAE.processInput("Bruno", "GoodEvent", "1.0", "The sun is shining");
    mAE.processInput("Anne", "BadActOther", "0.8", "Bruno is stealing money");

    // wait for 2 seconds
    try {
      Thread.currentThread().sleep(2000);
    } catch (InterruptedException ie) {
      log.warning("Sleep interrupted!");
    }

    // Test the new pause, step, and resume computation features
    log.info("Pause the affect computation of all characters");

    mAE.pauseAffectComputation();

    // wait for 5 seconds
    try {
      Thread.currentThread().sleep(5000);
    } catch (InterruptedException ie) {
      log.warning("Sleep interrupted!");
    }

    log.info("Performing 4 single affect computation steps of all characters, each every 750ms");
    mAE.stepwiseAffectComputation();
    // wait for 750 milliseconds
    try {
      Thread.currentThread().sleep(750);
    } catch (InterruptedException ie) {
      log.warning("Sleep interrupted!");
    }
    mAE.stepwiseAffectComputation();
    // wait for 750 milliseconds
    try {
      Thread.currentThread().sleep(750);
    } catch (InterruptedException ie) {
      log.warning("Sleep interrupted!");
    }
    mAE.stepwiseAffectComputation();
    // wait for 750 milliseconds
    try {
      Thread.currentThread().sleep(750);
    } catch (InterruptedException ie) {
      log.warning("Sleep interrupted!");
    }

    log.info("Resuming affect computation of all characters");
    mAE.resumeAffectComputation();

    log.info("Test bio signal input");

    // test the bio signal input
    mAE.processInput("Anne", "0.42", "0.42", "0.42", "0.5", "Bio Signal Input");

    // wait 30 seconds
    try {
      Thread.currentThread().sleep(30000);
    } catch (InterruptedException ie) {
      log.warning("Sleep interrupted!");
    }

    // stop affect processing and exit
    mAE.fAM.stopAll();
  }*/
}

