import javax.imageio.IIOException;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static StringBuilder tempLog = new StringBuilder();
    public static DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy ");
    public static LocalDateTime logWriteTime = LocalDateTime.now();

    public static void main(String[] args) {
        File src = dirCreator("src", "C:\\Users\\aszag\\IdeaProjects\\Games");
        File res = dirCreator("res", "C:\\Users\\aszag\\IdeaProjects\\Games");
        File savegames = dirCreator("savegames", "C:\\Users\\aszag\\IdeaProjects\\Games");
        File temp = dirCreator("temp", "C:\\Users\\aszag\\IdeaProjects\\Games");
        File main = dirCreator("main", "C:\\Users\\aszag\\IdeaProjects\\Games\\src");
        File test = dirCreator("test", "C:\\Users\\aszag\\IdeaProjects\\Games\\src");
        File drawables = dirCreator("drawables", "C:\\Users\\aszag\\IdeaProjects\\Games\\res");
        File vectors = dirCreator("vectors", "C:\\Users\\aszag\\IdeaProjects\\Games\\res");
        File icons = dirCreator("icons", "C:\\Users\\aszag\\IdeaProjects\\Games\\res");
        File log = fileCreator(temp, "temp.txt");

        try (FileWriter logWriter = new FileWriter(log, true)) {
            logWriter.write(tempLog.toString());
            logWriter.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        GameProgress saveOne = new GameProgress(99, 5, 2, 2.25);
        GameProgress saveTwo = new GameProgress(55, 10, 4, 5.25);
        GameProgress saveThree = new GameProgress(65, 12, 6, 10.25);

        saveGame("C:\\Users\\aszag\\IdeaProjects\\Games\\savegames\\saveOne.dat", saveOne);
        saveGame("C:\\Users\\aszag\\IdeaProjects\\Games\\savegames\\saveTwo.dat", saveTwo);
        saveGame("C:\\Users\\aszag\\IdeaProjects\\Games\\savegames\\saveThree.dat", saveThree);

        zipFiles("C:\\Users\\aszag\\IdeaProjects\\Games\\savegames\\zip_save.zip", savegames.listFiles());

        File[] files = savegames.listFiles((File pathname) -> pathname.getName().endsWith(".dat"));
        for(File file:files){
            file.delete();
        }

        openZip("C:\\Users\\aszag\\IdeaProjects\\Games\\savegames\\zip_save.zip", "C:\\Users\\aszag\\IdeaProjects\\Games\\savegames");

        GameProgress openProgress=openProgress("C:\\Users\\aszag\\IdeaProjects\\Games\\savegames\\saveThree.dat");
        System.out.println(openProgress);




    }


    static File dirCreator(String dirName, String pathName) {
        File newDir = new File(pathName + "\\" + dirName);
        if (newDir.mkdir()) {
            tempLog.append(logWriteTime.format(Formatter) + " Каталог " + newDir.getName() + " создан" + "\n");
        }
        return newDir;

    }

    static File fileCreator(File dirPath, String fileName) {
        File newFile = new File(dirPath, fileName);
        try {
            if (newFile.createNewFile()) {
                tempLog.append(logWriteTime.format(Formatter) + " Файл " + newFile.getName() + " создан" + "\n");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return newFile;
    }

    static void saveGame(String pathName, GameProgress save) {
        try (FileOutputStream fos = new FileOutputStream(pathName)) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(save);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    static void zipFiles(String pathName, File[] files) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathName))) {
            for (File file : files) {
                try (FileInputStream fis = new FileInputStream(file.getPath())) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        }

    static void openZip(String zipPath, String openZipPath){
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(openZipPath+ "\\"+ name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    static GameProgress openProgress(String save){
        GameProgress openProgress = null;
        try (FileInputStream fis = new FileInputStream(save);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            openProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return openProgress;
    }


}
