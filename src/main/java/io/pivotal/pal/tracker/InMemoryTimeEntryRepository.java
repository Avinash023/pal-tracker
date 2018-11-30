package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryTimeEntryRepository implements TimeEntryRepository{


   private Map<Long,TimeEntry> timeEntryMap= new HashMap<>();


    public TimeEntry find(long timeEntryId) {

          return timeEntryMap.get(timeEntryId);
    }


    public TimeEntry create(TimeEntry timeEntryToCreate) {

        timeEntryToCreate.setId(timeEntryMap.size()+1);

        timeEntryMap.put(timeEntryToCreate.getId(),timeEntryToCreate);
        return timeEntryToCreate;
    }


    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntryMap.values());
    }


    public TimeEntry update(long id, TimeEntry timeEntry) {

       timeEntryMap.put(id,timeEntry);
       timeEntry.setId(id);
        return timeEntry;
    }

    public ResponseEntity<TimeEntry> delete(long timeEntryId) {

        timeEntryMap.remove(timeEntryId);
        return new ResponseEntity<TimeEntry>(HttpStatus.OK);
    }
}
