package de.takacick.onenukeblock.utils.worldedit;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import de.takacick.onenukeblock.OneNukeBlock;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;

public class WorldEditUtils {

    private static String URI = "data/" + OneNukeBlock.MOD_ID + "/schematics";

    private static final TimedCache<String, Clipboard> CACHE = new TimedCache<>();

    public static Optional<Clipboard> getClipboard(String schematic) {
        if (CACHE.contains(schematic)) {
            return Optional.ofNullable(CACHE.get(schematic));
        }

        try {
            String location = URI + "/" + schematic;
            File file = File.createTempFile("src/main/resources/" + location, "file");
            FileUtils.copyInputStreamToFile(OneNukeBlock.class.getClassLoader().getResourceAsStream(location + ".schem"), file);
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            ClipboardReader reader = format.getReader(new FileInputStream(file));

            try {
                Clipboard clipboard = reader.read();

                CACHE.put(schematic, clipboard);

                return Optional.of(clipboard);
            } catch (Throwable var15) {
                var15.printStackTrace();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return Optional.empty();
    }

}
