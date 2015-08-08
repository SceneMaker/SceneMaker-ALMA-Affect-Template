package de.dfki.template;

//~--- non-JDK imports --------------------------------------------------------

import de.dfki.vsm.ProjectEditor;
import de.dfki.vsm.editor.Editor;
import de.dfki.vsm.model.sceneflow.SceneFlow;
import de.dfki.vsm.runtime.RunTime;
import de.dfki.vsm.runtime.value.AbstractValue;
import de.dfki.vsm.runtime.value.StructValue;

import static de.dfki.template.AlmaMonitor.log;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;

/**
 *
 * @author Sergio Soto
 */
public class VSM {
    ProjectEditor VSM = null;

    public VSM() {
        try {
            String args[] = new String[]{"res/prj/template/config.xml"};
            VSM = new ProjectEditor(args);   
            
        } catch (Exception e) {
            log.info("Error during VSM initialisation");
            System.exit(-1);
        }
    }

    public void setStructVSM(String name, HashMap<String, AbstractValue> map) {
        try {
            RunTime     mRunTime   = RunTime.getInstance();
            SceneFlow   mSceneFlow =
                Editor.getInstance().getSelectedProjectEditor().getSceneFlowEditor().getSceneFlow();
            StructValue struct     = new StructValue(map);

            mRunTime.setVariable(mSceneFlow, name, struct);
        } catch (Exception e) {}
    }

    public void setFloatVariableVSM(String name, Float value) {
        try {
            RunTime   mRunTime   = RunTime.getInstance();
            SceneFlow mSceneFlow = Editor.getInstance().getSelectedProjectEditor().getSceneFlowEditor().getSceneFlow();

            mRunTime.setVariable(mSceneFlow, name, value);              
        } catch (Exception e) {}
    }
}
