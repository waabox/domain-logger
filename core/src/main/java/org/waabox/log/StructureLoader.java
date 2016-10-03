package org.waabox.log;

import org.apache.commons.lang3.Validate;

import org.json.*;

import java.io.*;
import java.net.URL;
import java.util.*;

import net.sf.corn.cps.*;

/** Search within the classpath the *log_structure.json and generates
 * the Structures.
 *
 * @author waabox (me[at]waabox[dot]org)
 */
public final class StructureLoader {

  /** The singleton instance, it's never null.*/
  private static final StructureLoader INSTANCE = new StructureLoader();

  /** Map that holds the structures by its name, it's never null.*/
  private final Map<String, Structure> structures = new LinkedHashMap<>();

  /** Creates a new instance of the loader.*/
  private StructureLoader() {
    ResourceFilter filter = new ResourceFilter().archiveName("*");
    List<URL> urls = CPScanner.scanResources(filter);
    for (URL url : urls) {
      if (!url.getFile().contains("log_structure.json")) {
        continue;
      }
      BufferedReader reader = null;
      StringBuilder sb = new StringBuilder();
      String line;
      try {
        reader = new BufferedReader(new InputStreamReader(url.openStream()));
        while ((line = reader.readLine()) != null) {
          sb.append(line);
        }
        JSONArray jsonStructures = new JSONArray(sb.toString());
        for (int i = 0; i < jsonStructures.length(); i++) {
          JSONObject obj = jsonStructures.getJSONObject(i);
          String name = obj.getString("name");
          String stack = obj.getString("stack");
          structures.put(name, new Structure(name, stack));
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      } finally {
        if (reader != null) {
          try {
            reader.close();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
  }

  /** Retrieves an structure by its name.
   * @param name the name of the structure.
   * @return the Structure, never null.
   */
  public static Structure named(final String name) {
    Validate.isTrue(INSTANCE.structures.containsKey(name),
        "Structure: " + name + " not found");
    return INSTANCE.structures.get(name);
  }

}
