package com.jazara.icu.auth.controller;

import com.jazara.icu.auth.domain.Branch;
import com.jazara.icu.auth.service.BranchService;
import com.jazara.icu.auth.service.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/api/branch")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class BranchController {

    @Autowired
    CustomResponse customResponse;

    @Autowired
    private BranchService branchService;

    @PostMapping(value = "/add")
    public ResponseEntity<Map<String, Object>> createBranch(@RequestBody Branch branch) {
        final Branch b = branchService.createBranch(branch);
        if (b == null) {
            return customResponse.HandleResponse(false, "error creating branch", "", HttpStatus.OK);
        }
        return customResponse.HandleResponse(true, "", b, HttpStatus.OK);
    }

    @PutMapping(value = "/edit/{id}")
    public ResponseEntity<Map<String, Object>> editBranch(@PathVariable Long id, @RequestBody Branch branch) {
        Branch b = branchService.editBranch(branch);
        if (b == null) {
            return customResponse.HandleResponse(false, "error updating branch info", "", HttpStatus.OK);
        }
        return customResponse.HandleResponse(true, "", b, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<Map<String, Object>> getBranchesByOwnerId(@PathVariable Long id) {
        Map<String, Object> tokenMap = new HashMap<String, Object>();
        final ArrayList<Branch> branches = branchService.getBranchesByOwnerId(id);
        tokenMap.put("branches", branches);
        return customResponse.HandleResponse(true, "", tokenMap, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Map<String, Object>> getBranch(@PathVariable Long id) {
        final Optional<Branch> b = branchService.getBranchById(id);
        if (!b.isPresent()) {
            return customResponse.HandleResponse(false, "branch not found", "", HttpStatus.OK);
        }
        return customResponse.HandleResponse(true, "", b, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Map<String, Object>> deleteBranch(@PathVariable Long id) {
        if (branchService.deleteBranchById(id))
            return customResponse.HandleResponse(true, "", "", HttpStatus.OK);
        return customResponse.HandleResponse(false, "cannot delete branch", "", HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/deleteAll")
    public ResponseEntity<Map<String, Object>> deleteAllBranches() {
        try {
            branchService.deleteAllBranches();
            return customResponse.HandleResponse(true, "deleted all branches", "", HttpStatus.OK);
        } catch (Exception e) {
            return customResponse.HandleResponse(false, e.toString(), "", HttpStatus.OK);
        }
    }
}
