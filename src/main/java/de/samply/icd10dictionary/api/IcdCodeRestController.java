package de.samply.icd10dictionary.api;

import de.samply.icd10dictionary.model.IcdCode;
import de.samply.icd10dictionary.service.CodeSystem;
import de.samply.icd10dictionary.service.LoadIcdCodeService;
import de.samply.icd10dictionary.service.SearchIcdCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/icd")
@RestController
public class IcdCodeRestController {

    private final LoadIcdCodeService loadIcdCodeService;
    private final SearchIcdCodeService searchIcdCodeService;

    @Autowired
    public IcdCodeRestController(LoadIcdCodeService loadIcdCodeService, SearchIcdCodeService searchIcdCodeService) {
        this.loadIcdCodeService = loadIcdCodeService;
        this.searchIcdCodeService = searchIcdCodeService;
    }

    @PostMapping("load")
    public void load(@RequestBody CodeSystem codeSystem) {
        this.loadIcdCodeService.load(codeSystem);
    }

    @GetMapping
    public List<IcdCode> retrieveCodesBySearchword(@RequestParam String searchword) {
        return this.searchIcdCodeService.retrieveCodesBySearchword(searchword);
    }

}
