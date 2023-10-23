package model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//today i set out to create a 3d linked list structure. i have truly fallen into hell. god help me - 10/22/2023
public class LinkedCube implements Comparable<Cube>, Serializable  {

    private LinkedCube positiveX;
    private LinkedCube negativeX;
    private LinkedCube positiveY;
    private LinkedCube negativeY;
    private LinkedCube positiveZ;
    private LinkedCube negativeZ;

    private final Point point;

    private int euclideanDistancesSum;
    private int manhattanDistancesSum;

    private int hashCode;

    public LinkedCube(int x, int y, int z) {
        this.point = new Point(x, y, z);
        this.euclideanDistancesSum = 0;
        this.manhattanDistancesSum = 0;

        this.hashCode = 0;
    }

    public LinkedCube(Point point) {
        this(point.x(), point.y(), point.z());
    }

    public LinkedCube(LinkedCube linkedCube) {
        this.point = new Point(linkedCube.point.x(), linkedCube.point.y(), linkedCube.point.z());
        this.euclideanDistancesSum = linkedCube.euclideanDistancesSum;
        this.manhattanDistancesSum = linkedCube.manhattanDistancesSum;
        this.hashCode = linkedCube.hashCode;
    }

    public List<Point> getEmptyNeighbors() {
        List<Point> emptyNeighbors = new ArrayList<>();

        if(positiveX == null) emptyNeighbors.add(new Point(point.x() + 1, point.y(), point.z()));
        if(negativeX == null) emptyNeighbors.add(new Point(point.x() - 1, point.y(), point.z()));

        if(positiveY == null) emptyNeighbors.add(new Point(point.x(), point.y() + 1, point.z()));
        if(negativeY == null) emptyNeighbors.add(new Point(point.x() , point.y() - 1, point.z()));

        if(positiveZ == null) emptyNeighbors.add(new Point(point.x(), point.y(), point.z() + 1));
        if(negativeZ == null) emptyNeighbors.add(new Point(point.x(), point.y(), point.z() - 1));

        return emptyNeighbors;
    }

    public List<LinkedCube> cloneStructure() {
        HashMap<LinkedCube, LinkedCube> visited = new HashMap<>();
        List<LinkedCube> clonedCubes = new ArrayList<>();
        recursiveClone(this, visited, clonedCubes);
        return clonedCubes;
    }

    //unspeakable evil. lord forgive me for i have forsaken my oath. the temptation of the dark magic was too much to bear as i writhed in the dirt. i will sacrafice evertying to achive my goal....
    private static LinkedCube recursiveClone(LinkedCube node, HashMap<LinkedCube, LinkedCube> visited, List<LinkedCube> clonedNodes) {
        if (node == null) return null;

        // If the node is already cloned, return the cloned version.
        if (visited.containsKey(node)) {
            return visited.get(node);
        }

        LinkedCube newNode = new LinkedCube(node);
        visited.put(node, newNode);
        clonedNodes.add(newNode);

        // Recursively clone neighbors.
        newNode.positiveX = recursiveClone(node.positiveX, visited, clonedNodes);
        newNode.negativeX = recursiveClone(node.negativeX, visited, clonedNodes);
        newNode.positiveY = recursiveClone(node.positiveY, visited, clonedNodes);
        newNode.negativeY = recursiveClone(node.negativeY, visited, clonedNodes);
        newNode.positiveZ = recursiveClone(node.positiveZ, visited, clonedNodes);
        newNode.negativeZ = recursiveClone(node.negativeZ, visited, clonedNodes);

        return newNode;
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = (String.valueOf(manhattanDistancesSum) + euclideanDistancesSum).hashCode();
        }
        return this.hashCode;
    }

    public void addManhattanDistance(int distance) {
        this.hashCode = 0;
        this.manhattanDistancesSum += distance;
    }

    public void addEuclideanDistancesSum(int distance) {
        this.hashCode = 0;
        this.euclideanDistancesSum += distance;
    }

    //setters
    public void setPositiveX(LinkedCube positiveX) {
        this.positiveX = positiveX;
    }

    public void setNegativeX(LinkedCube negativeX) {
        this.negativeX = negativeX;
    }

    public void setPositiveY(LinkedCube positiveY) {
        this.positiveY = positiveY;
    }

    public void setNegativeY(LinkedCube negativeY) {
        this.negativeY = negativeY;
    }

    public void setPositiveZ(LinkedCube positiveZ) {
        this.positiveZ = positiveZ;
    }

    public void setNegativeZ(LinkedCube negativeZ) {
        this.negativeZ = negativeZ;
    }

    //getters
    public LinkedCube getPositiveX() {
        return positiveX;
    }

    public LinkedCube getNegativeX() {
        return negativeX;
    }

    public LinkedCube getPositiveY() {
        return positiveY;
    }

    public LinkedCube getNegativeY() {
        return negativeY;
    }

    public LinkedCube getPositiveZ() {
        return positiveZ;
    }

    public LinkedCube getNegativeZ() {
        return negativeZ;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public int compareTo(Cube other) {
        return this.hashCode() - other.hashCode();
    }

    @Override
    public String toString() {
        String cube = "";

        cube += "Point: " + point.toString();
        cube += "\nEuclidean Sum: " + this.euclideanDistancesSum;
        cube += "\nManhattan Sum: " + this.manhattanDistancesSum;
        cube += "\nHashcode: " + this.hashCode;

        return cube;
    }
}
