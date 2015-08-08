package de.dfki.template;

//~--- non-JDK imports --------------------------------------------------------

import de.affect.manage.AffectManager;
import de.affect.manage.event.AffectUpdateEvent;
import de.affect.manage.event.AffectUpdateListener;
import de.affect.util.AppraisalTag;
import de.affect.xml.AffectInputDocument.AffectInput;
import de.affect.xml.AffectOutputDocument;
import de.affect.xml.AffectOutputDocument.AffectOutput.CharacterAffect;
import de.affect.xml.EmotionType;
import de.dfki.vsm.editor.Editor;


import de.dfki.vsm.runtime.value.AbstractValue;
import de.dfki.vsm.runtime.value.FloatValue;

import org.apache.xmlbeans.XmlException;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 *
 * @author Tengfei Wang & Sergi Soto
 */

/**
 * <code>AffectEngine</code> shows a typical way using ALMA. The example class
 * provides a method for handling affect input and handles the computed affect.
 */
public class AlmaMonitor implements AffectUpdateListener {

    // The ALMA Java implementation
    public static AffectManager fAM = null;

    // ALMA configuration files
    private static final String sALMACOMP = "conf/AffectComputationExample.aml";
    private static final String sALMADEF  = "conf/AffectDefinitionExample.aml";

    // ALMA mode:
    // false - output on console
    // true - graphical user interface CharacterBuilder
    // NOTE: No runtime windows (defined in AffectComputation or
    // AffectDefinition will be displayed!)
    private static final boolean sGUIMode = true;

    // Console logging
    public static final Logger log = Logger.getLogger("Alma");

    // Affects Map
    final HashMap<String, AbstractValue> mAffectsMap = new HashMap<>();

    // VisualSceneMaker
    private VSM SceneMakerInstance;

    public AlmaMonitor() {

        // Starting the ALMA affect engine
        try {
            
            SceneMakerInstance = new VSM();         
               
            fAM = new AffectManager(sALMACOMP, sALMADEF, sGUIMode);
            fAM.addAffectUpdateListener(this);           
            
        } catch (IOException io) {
            log.info("Error during ALMA initialisation");
            System.exit(-1);
        } catch (XmlException xmle) {
            log.info("Error in ALMA configuration");
            System.exit(-1);
        }
    }

    /**
     * Listens to affect updates computed by ALMA. This implements the
     * AffectUpdateListener
     */
    @Override
    public synchronized void update(AffectUpdateEvent event) {
        AffectOutputDocument aod = event.getUpdate();

      //  mAffectsMap.clear();
        log.info(">>>");

        try {
            for (CharacterAffect character : aod.getAffectOutput().getCharacterAffectList()) {
                // access cached data or create new cache
                String name       = character.getName();
                String emotion    = character.getDominantEmotion().getName().toString();
                double eIntensity = Double.parseDouble(character.getDominantEmotion().getValue());
                String mood       = character.getMood().getMoodword().toString();
                String mIntensity = character.getMood().getIntensity().toString();
                String mTendency  = character.getMoodTendency().getMoodword().toString();

                // TODO use affect for something!
                log.info(name + " has dominant emotion " + emotion + "(" + eIntensity + ")");

                // get the intensity of all active emotions of the character
                for (EmotionType et : character.getEmotions().getEmotionList()) {
                    log.info(name + "'s " + et.getName().toString() + " has intensity " + et.getValue());
                    
                    if(name.equals("Anne")){
                        mAffectsMap.put(et.getName().toString(), new FloatValue(Float.parseFloat(et.getValue())));
                        
                    
                        try{
                            SceneMakerInstance.setFloatVariableVSM(et.getName().toString(), Float.parseFloat(et.getValue()));
                            SceneMakerInstance.setStructVSM("Affects", mAffectsMap);
                        }

                        catch (Exception e) {
                           // System.out.println("SetVariable " + et.getName().toString() + " to " + Float.parseFloat(et.getValue()) + " : VSM is not running");
                        }
                      }
                }
            }
        } catch (Exception e) {
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
    public static void processInput(String character, String input, String intensity, String elicitor) {
        AffectInput ai = AppraisalTag.instance().makeAffectInput(character, input, intensity, elicitor);

        // debug    log.info(ai.toString());
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

        // debug   log.info(ai.toString());
        fAM.processSignal(ai);
    }

    /**
     * Wrapper method for the call for pausing the affect computation
     */
    public void pauseAffectComputation() {
        AffectManager.sInterface.pauseAffectComputation();

        // It is also possible to pause the affect computation of a single character /group
        // fAM.sInterface.pauseAffectComputation("Anne");
    }

    /**
     * Wrapper method for the call for resuming a paused affect computation
     */
    public void resumeAffectComputation() {
        AffectManager.sInterface.resumeAffectComputation();

        // It is also possible to resume a paused  affect computation of a single character /group
        // fAM.sInterface.resumeAffectComputation("Anne");
    }

    /**
     * Wrapper method for the call for a stepwise execution of a paused affect
     * computation
     */
    public void stepwiseAffectComputation() {
        AffectManager.sInterface.stepwiseAffectComputation();

        // It is also possible to stepwise execute a pause affect computation of a single character /group
        // fAM.sInterface.stepwiseAffectComputation("Anne");
    }
    
    public VSM getVSM(){
        return SceneMakerInstance;
    }
    
    public  AlmaMonitor getInstance(){
        return this;
    }
    
   

}

