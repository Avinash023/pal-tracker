package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    TimeEntryRepository timeEntryRepository;
    private final CounterService counter;
    private final GaugeService gauge;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, CounterService counter, GaugeService gauge) {
    this.timeEntryRepository=timeEntryRepository;
    this.counter = counter;
    this.gauge = gauge;
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {
        TimeEntry tm=timeEntryRepository.create(timeEntry);
        System.out.println("Creating Entity " + tm.getId());
        counter.increment("TimeEntry.created");
        gauge.submit("timeEntries.count", timeEntryRepository.list().size());
        return new ResponseEntity<TimeEntry>(tm, HttpStatus.CREATED);
    }


    @GetMapping("{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId){

        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        if (timeEntry == null) {
            System.out.println("timeEntry with id " + timeEntryId + " not found");
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }
        counter.increment("TimeEntry.read");
        return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
    }

    @DeleteMapping("{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long timeEntryId) {
        System.out.println("Fetching & Deleting TimeEntry with id " + timeEntryId);
        System.out.println(timeEntryRepository.list());
       // TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        timeEntryRepository.delete(timeEntryId);
       /* if (timeEntry == null) {
            System.out.println("Unable to delete. TimeEntry with id " + timeEntryId + " not found");
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }*/
        counter.increment("TimeEntry.deleted");
        gauge.submit("timeEntries.count", timeEntryRepository.list().size());
        return new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
    }


    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> timeEntryList=timeEntryRepository.list();

/*
        if(timeEntryList.isEmpty()){
            return new ResponseEntity<List<TimeEntry>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
*/
        counter.increment("TimeEntry.listed");
        return new ResponseEntity<List<TimeEntry>>(timeEntryList, HttpStatus.OK);
    }


    @PutMapping("{timeEntryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable long timeEntryId, @RequestBody TimeEntry timeEntry) {

        System.out.println("Updating Time Entry " + timeEntryId);

        TimeEntry curTimeEntry = timeEntryRepository.update(timeEntryId,timeEntry);

        if (curTimeEntry==null) {
            System.out.println("timeEntry with timeEntryId " + timeEntryId + " not found");
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }


        timeEntry.setId(timeEntryId);
        counter.increment("TimeEntry.updated");
        //timeEntryRepository.update(timeEntryId,timeEntry);
        return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
    }
}
