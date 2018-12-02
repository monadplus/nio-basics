import java.io.IOException
import java.nio.file._

object FilesExample {
  val absPath = Paths.get("Users/arnau/Dropbox/Epubs/haskell/haskellBook.pdf")
  val relPath = Paths.get("Users/arnau/IdeaProjects/nio", "lore.txt")
  // delete all . and .. of the path resolving the route
  relPath.normalize()

  Files.exists(absPath, LinkOption.NOFOLLOW_LINKS)
  try {
    Files.createDirectory(Paths.get("Users/arnau/IdeaProjects/nio/deleteme"))
  } catch {
    case f: FileAlreadyExistsException => ???
    case io: IOException               => ???
  }

  // Files.copy (you can overwrite)
  // Files.move
  // Files.delete
  // Files.walkFileTree
  // Example: searching for files

  // Path rootPath = Paths.get("data");
  //String fileToFind = File.separator + "README.txt";
  //
  //try {
  //  Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
  //
  //    @Override
  //    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
  //      String fileString = file.toAbsolutePath().toString();
  //      //System.out.println("pathString = " + fileString);
  //
  //      if(fileString.endsWith(fileToFind)){
  //        System.out.println("file found at path: " + file.toAbsolutePath());
  //        return FileVisitResult.TERMINATE;
  //      }
  //      return FileVisitResult.CONTINUE;
  //    }
  //  });
  //} catch(IOException e){
  //    e.printStackTrace();
  //}

}
