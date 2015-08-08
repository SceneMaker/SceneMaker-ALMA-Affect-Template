package de.dfki.template;

//~--- non-JDK imports --------------------------------------------------------

import de.dfki.vsm.editor.event.SceneExecutedEvent;
import de.dfki.vsm.editor.event.TurnExecutedEvent;
import de.dfki.vsm.editor.event.UtteranceExecutedEvent;
import de.dfki.vsm.model.configs.PlayerConfig;
import de.dfki.vsm.model.project.ProjectData;
import de.dfki.vsm.model.script.AbstractWord;
import de.dfki.vsm.model.script.ActionObject;
import de.dfki.vsm.model.script.SceneAbbrev;
import de.dfki.vsm.model.script.SceneGroup;
import de.dfki.vsm.model.script.SceneObject;
import de.dfki.vsm.model.script.SceneParam;
import de.dfki.vsm.model.script.SceneScript;
import de.dfki.vsm.model.script.SceneTurn;
import de.dfki.vsm.model.script.SceneUttr;
import de.dfki.vsm.model.script.SceneWord;
import de.dfki.vsm.runtime.Process;
import de.dfki.vsm.runtime.player.SceneGroupPlayer;
import de.dfki.vsm.runtime.value.AbstractValue;
import de.dfki.vsm.runtime.value.AbstractValue.Type;
import de.dfki.vsm.runtime.value.StringValue;
import de.dfki.vsm.runtime.value.StructValue;
import de.dfki.vsm.util.evt.EventCaster;
import de.dfki.vsm.util.log.LOGDefaultLogger;


//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 * @author Gregor Mehlmann
 */
public class TemplateSceneGroupPlayer implements SceneGroupPlayer {

    // The Logger Instance
    private final LOGDefaultLogger mLogger = LOGDefaultLogger.getInstance();

    // The Player Properties
    private final PlayerConfig mProperties;

    // The Current Project
    private final ProjectData mProject;

    // Construct A Default Player
    public TemplateSceneGroupPlayer(final ProjectData project) {
        mProject    = project;
        mProperties = project.getScenePlayerProperties();
    }

    // Launch The Default Player
    @Override
    public final void launch() {}

    // Unload The Default Player
    @Override
    public final void unload() {}

    @Override
    public final void play(final String name, final LinkedList<AbstractValue> args) {
        final Process                 process        = ((Process) java.lang.Thread.currentThread());
        final HashMap<String, String> mSceneParamMap = new HashMap<String, String>();

        // Process The Arguments
        if ((args != null) &&!args.isEmpty()) {

            // Get The First Argument
            final AbstractValue value = args.getFirst();

            // Check The Argument Type
            if (value.getType().equals(Type.STRUCT)) {

                // Cast The Argument Type
                final StructValue struct = (StructValue) value;

                // Process Scene Arguments
                for (final Entry<String, AbstractValue> entry : struct.getValueMap().entrySet()) {
                    if (entry.getValue().getType() == Type.STRING) {
                        mSceneParamMap.put(entry.getKey(), ((StringValue) entry.getValue()).getValue());
                    } else {

                        // Process Other Argument Types
                    }
                }
            }
        }

        Task task;
        task = new Task(process.getName() + name) {
            @Override
            public void run() {

                // Select The Scene
                final SceneScript script = mProject.getSceneScript();
                final SceneGroup  group  = script.getSceneGroup(name);
                final SceneObject scene  = group.select();

                // Scene Visualization
                mLogger.message("Executing scene:\r\n" + scene.getText());
                EventCaster.getInstance().convey(new SceneExecutedEvent(this, scene));

                // Process The Turns
                for (SceneTurn turn : scene.getTurnList()) {

                    // Turn Visualization
                    mLogger.message("Executing turn:" + turn.getText());
                    EventCaster.getInstance().convey(new TurnExecutedEvent(this, turn));

                    // Get The Turn Speaker
                    final String speaker = turn.getSpeaker();

                    if (speaker == null) {

                        // Get The Default Speaker
                    }

                    // Count The Word Number
                    int wordCount = 0;

                    // Process Utterance
                    for (SceneUttr utt : turn.getUttrList()) {

                        // Utterance Visualization
                        mLogger.message("Executing utterance:" + utt.getText());
                        EventCaster.getInstance().convey(new UtteranceExecutedEvent(this, utt));

                        // Process the words of this utterance
                        for (AbstractWord word : utt.getWordList()) {
                            if (word instanceof SceneWord) {

                                // Visualization
                                mLogger.message("Executing vocable:" + ((SceneWord) word).getText());
                                wordCount = ((SceneWord) word).getText().length();
                            } else if (word instanceof SceneParam) {

                                // Visualization
                                mLogger.message("Executing param:" + ((SceneParam) word).getText());
                            } else if (word instanceof ActionObject) {
                                
                                String ActionTag = ((ActionObject) word).getText();
                               // ActionTag = ActionTag.replaceAll("[", "")；
                                
                                
                              String ch = ActionTag.substring(1, ActionTag.length()-1);
                               //ActionTag.replace('[', '');
                                //ActionTag.replace(']', '');
                                
                                AlmaMonitor.processInput("Anne", ch , "0.8", "action");
                                
                                // if(((ActionObject) word).getText().equals("[GoodActSelf]")){
                                //    AlmaMonitor.processInput("Anne", "GoodActSelf", "0.8", "how are you");
                                //}
                                // Visualization
                                mLogger.message("Executing action:" + ch );
                            } else if (word instanceof SceneAbbrev) {

                                // Visualization
                                mLogger.message("Executing abbreviation:" + ((SceneAbbrev) word).getText());
                            }
                        }
                    }

                    // Utterance Simulation
                    try {
                        sleep(wordCount * 100);
                    } catch (InterruptedException exc) {
                        mLogger.warning(exc.toString());
                    }

                    // Exit If Interrupted
                    if (mIsDone) {
                        return;
                    }
                }
            }
        };

        // Start The Player Task
        task.start();

        // Wait For Termination
        boolean finished = false;

        while (!finished) {
            try {

                // Join The Player Task
                task.join();

                // Finish This Execution
                finished = true;
            } catch (Exception e) {

                // Abort The Player Task
                task.mIsDone = true;
            }
        }
    }
}
