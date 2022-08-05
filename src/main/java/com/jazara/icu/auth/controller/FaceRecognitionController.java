package com.jazara.icu.auth.controller;

import com.jazara.icu.auth.domain.Person;
import com.jazara.icu.auth.payload.FaceRequest;
import com.jazara.icu.auth.service.FaceService;
import com.jazara.icu.auth.service.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/api/ai")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class FaceRecognitionController {

    @Autowired
    CustomResponse customResponse;

    @Autowired
    private FaceService faceService;

    @PostMapping(value = "/newfaces")
    public ResponseEntity<Map<String, Object>> createPerson(@RequestBody FaceRequest faceRequest) throws Exception {
        try {
            return customResponse.HandleResponse(true, null, faceService.addFaces(faceRequest), HttpStatus.OK);
        } catch (Exception e) {
            return customResponse.HandleResponse(false, e.getMessage(), null, HttpStatus.OK);

        }
    }

    @PostMapping(value = "/search")
    public ResponseEntity<Map<String, Object>> SearchByFace(@RequestBody List<Double> face) throws Exception {
        try {
            Person p = faceService.checkFaceExistance(face);
            if (p != null)
                return customResponse.HandleResponse(true, null, p, HttpStatus.OK);
            else
                return customResponse.HandleResponse(false, "face not found", null, HttpStatus.OK);
        } catch (Exception e) {
            return customResponse.HandleResponse(false, e.getMessage(), null, HttpStatus.OK);

        }
    }
}
