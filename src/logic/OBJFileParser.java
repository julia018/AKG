package logic;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OBJFileParser {


    private static final String POSITION = "^v\\s+([\\d\\.\\+\\-eE]+)\\s+([\\d\\.\\+\\-eE]+)\\s+([\\d\\.\\+\\-eE]+)";
    private static final String NORMAL = "^vn\\s+([\\d\\.\\+\\-eE]+)\\s+([\\d\\.\\+\\-eE]+)\\s+([\\d\\.\\+\\-eE]+)";
    private static final String UV = "^vt\\s+([\\d\\.\\+\\-eE]+)\\s+([\\d\\.\\+\\-eE]+)";
    private static final String FACE = "^f\\s+(-?\\d+)\\/(-?\\d+)\\/(-?\\d+)\\s+(-?\\d+)\\/(-?\\d+)\\/(-?\\d+)\\s+(-?\\d+)\\/(-?\\d+)\\/(-?\\d+)(?:\\s+(-?\\d+)\\/(-?\\d+)\\/(-?\\d+))?";

    public static void main(String [] args) {
        try {
            parseOBJFile(new File("res/sphere.obj"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Geometry parseOBJFile(File objFile) throws IOException {
        List<Vector3> positions = new ArrayList<>();
        List<Vector3> normals = new ArrayList<>();
        List<Vector2> uvs = new ArrayList<>();
        List<Triangle> faces = new ArrayList<>();
        FileReader fileReader = new FileReader(objFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;

        while((line = bufferedReader.readLine()) != null) {
            processString(positions, normals, uvs, faces, line);
        }
        System.out.println(faces.size());
        return null;
    }

    private static void processString(List<Vector3> positions, List<Vector3> normals, List<Vector2> uvs, List<Triangle> faces, String line) {
        // Create Pattern objects
        Pattern positionPattern = Pattern.compile(POSITION);
        Pattern normalPattern = Pattern.compile(NORMAL);
        Pattern uvPattern = Pattern.compile(UV);
        Pattern facePattern = Pattern.compile(FACE);

        // Create matcher objects
        Matcher positionMatcher = positionPattern.matcher(line);
        Matcher normalMatcher = normalPattern.matcher(line);
        Matcher uvMatcher = uvPattern.matcher(line);
        Matcher faceMatcher = facePattern.matcher(line);

        if(positionMatcher.find()) {

            positions.add(new Vector3(Float.parseFloat(positionMatcher.group(1)), Float.parseFloat(positionMatcher.group(2)), Float.parseFloat(positionMatcher.group(3))));

        } else if(normalMatcher.find()) {

            normals.add(new Vector3(Float.parseFloat(normalMatcher.group(1)), Float.parseFloat(normalMatcher.group(2)), Float.parseFloat(normalMatcher.group(3))));

        } else if(uvMatcher.find()) {

            uvs.add(new Vector2(Float.parseFloat(uvMatcher.group(1)), Float.parseFloat(uvMatcher.group(2))));

        } else if(faceMatcher.find()) {
            List<Vertex> vertices = new ArrayList<>();
            for (int i = 1; i < 10; i += 3) {
                Vector3 position = positions.get(Integer.parseInt(faceMatcher.group(i)) - 1);
                Vector2 uv = uvs.get(Integer.parseInt(faceMatcher.group(i + 1)) - 1);
                Vector3 normal = normals.get(Integer.parseInt(faceMatcher.group(i + 2)) - 1);
                vertices.add(new Vertex(position, normal, uv));
                System.out.println("Vertex ");
            }
            faces.add(new Triangle(vertices));
        }

    }


}
