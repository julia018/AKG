package logic;

import model.Vector3;

public class Lambert {
    public Vector3 getSource() {
        return source;
    }

    private Vector3 source;


    public Lambert(Vector3 source) {
        this.source = source;
        //this.light = source.substractVector(target).getNormalized();
    }

}
