package editor;

import components.NonClickable;
import imgui.ImGui;
import jade.GameObject;
import jade.MouseListener;
import renderer.PickingTexture;
import scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    private float debounce = 0.2f;

    public PropertiesWindow(PickingTexture pickingTexture){
        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currentScene){
        debounce -= dt;

        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce <= 0) {
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();
            int activeGameObjectID = pickingTexture.readPixel(x, y);
            GameObject pickedObj = currentScene.getGameObject(activeGameObjectID);
            if (pickedObj != null && pickedObj.getComponent(NonClickable.class) == null){
                activeGameObject = pickedObj;
            } else if (pickedObj == null && !MouseListener.isDragging()) {
                activeGameObject = null;
            }
            this.debounce = 0.2f;
        }
    }

    public void imgui(){
        if (activeGameObject != null) {
            ImGui.begin("Properties");
            activeGameObject.imgui();
            ImGui.end();
        }
    }

    public GameObject getActiveGameObject() {
        return this.activeGameObject;
    }
}
