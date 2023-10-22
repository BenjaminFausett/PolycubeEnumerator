import controller.PolycubeCalculator;
import model.PolycubeRepository;

public class Main {

    public static void main(String[] args) throws Exception {

        //PolycubeRepository.printBackupFileSizes();

        PolycubeCalculator.calculatePolycubes(11);
    }
}