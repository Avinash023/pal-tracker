package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class TimeEntryController {



    TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
    this.timeEntryRepository=timeEntryRepository;
    }
    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {
        TimeEntry tm=timeEntryRepository.create(timeEntry);
        System.out.println("Creating Entity " + tm.getId());
        return new ResponseEntity<TimeEntry>(tm, HttpStatus.CREATED);
    }


    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId){

        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        if (timeEntry == null) {
            System.out.println("timeEntry with id " + timeEntryId + " not found");
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long timeEntryId) {
        System.out.println("Fetching & Deleting TimeEntry with id " + timeEntryId);
        System.out.println(timeEntryRepository.list());
       // TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        timeEntryRepository.delete(timeEntryId);
       /* if (timeEntry == null) {
            System.out.println("Unable to delete. TimeEntry with id " + timeEntryId + " not found");
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }*/

        return new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> timeEntryList=timeEntryRepository.list();

        if(timeEntryList.isEmpty()){
            return new ResponseEntity<List<TimeEntry>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<TimeEntry>>(timeEntryList, HttpStatus.OK);
    }


    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable long timeEntryId, @RequestBody TimeEntry timeEntry) {

        System.out.println("Updating Time Entry " + timeEntryId);

        TimeEntry curTimeEntry = timeEntryRepository.update(timeEntryId,timeEntry);

        if (curTimeEntry==null) {
            System.out.println("timeEntry with timeEntryId " + timeEntryId + " not found");
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }


        timeEntry.setId(timeEntryId);
        //timeEntryRepository.update(timeEntryId,timeEntry);
        return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
    }
}
