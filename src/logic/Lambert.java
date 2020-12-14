package logic;

import model.Vector3;

public class Lambert {
    public Vector3 getSource() {
        return source;
    }

    private Vector3 source;
    private Vector3 invertedLight;


    public Lambert(Vector3 source) {
        this.source = source;
        //this.light = source.substractVector(target).getNormalized();
        this.invertedLight = new Vector3(source.getX() * -1, source.getY() * -1, source.getZ() * -1);
    }

    public Vector3 getInvertedLight() {
        return invertedLight;
    }
}
