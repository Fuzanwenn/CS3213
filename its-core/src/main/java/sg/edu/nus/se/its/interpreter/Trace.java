package sg.edu.nus.se.its.interpreter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import sg.edu.nus.se.its.model.Memory;

/**
 * Execution trace as result of program execution during interpretation.
 */
public class Trace implements Iterable<TraceEntry> {
  private List<TraceEntry> entries = new ArrayList<>();

  public void add(String fnc, int loc, Memory mem) {
    entries.add(new TraceEntry(fnc, loc, mem));
  }

  /**
   * Returns the matching TraceEntry for the given location id. We iterate trace entries in a way
   * that every TracEntry object is only returned once for the matching location. If a TraceEntry
   * was already returned then this method would search for the next matching entry.
   *
   * @param location - program location to match
   * @return TraceEntry object
   */
  public TraceEntry getUnique(int location) {
    for (TraceEntry entry : getEntries()) {
      if (entry.getLocation() == location && entry.setChecked()) {
        return entry;
      }
    }
    return null;
  }

  /**
   * Returns the matching TraceEntry for the given location id and function name.
   * We iterate trace entries in a way that every TracEntry object is only returned
   * once for the matching location. If a TraceEntry was already returned
   * then this method would search for the next matching entry.
   *
   * @param functionName - the function name of the trace entry to be found
   * @param location - program location to match
   * @return TraceEntry object
   */
  public TraceEntry getUnique(String functionName, int location) {
    for (TraceEntry entry : getEntries()) {

      if (entry.getLocation() == location && entry.setChecked()
          && entry.getFunctionName().equals(functionName)) {
        return entry;
      }
    }
    return null;
  }

  /**
   * Returns all matching TraceEntry objects for the given location id.
   *
   * @param location - program location to match
   * @return list of TraceEntry objects
   */
  public List<TraceEntry> getAll(int location) {
    List<TraceEntry> matchingTraceEntry = new ArrayList<>();
    for (TraceEntry entry : getEntries()) {
      if (entry.getLocation() == location) {
        matchingTraceEntry.add(entry);
      }
    }
    return matchingTraceEntry;
  }


  public TraceEntry getLastEntry() {
    return entries.get(entries.size() - 1);
  }

  @Override
  public Iterator<TraceEntry> iterator() {
    return entries.iterator();
  }

  @Override
  public String toString() {
    String entryString =
        entries.stream().map(TraceEntry::toString).collect(Collectors.joining(", "));
    return String.format("Trace [%s]", entryString);
  }

  public List<TraceEntry> getEntries() {
    return entries;
  }

  public int size() {
    return entries.size();
  }
}
