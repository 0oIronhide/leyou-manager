package cn.hp.item.controller;

import cn.hp.item.pojo.SpecGroup;
import cn.hp.item.pojo.SpecParam;
import cn.hp.item.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ironhide
 * @create 2020-04-30-10:21
 */
@RequestMapping("spec")
@RestController
public class SpecController {

    @Autowired
    private SpecService service;

    /**
     * 查询规格组
     *
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroup(@PathVariable("cid") Long cid) {
        List<SpecGroup> specGroups = service.querySpecGroup(cid);
        if (specGroups == null || specGroups.size() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(specGroups);
    }

    /**
     * 新增规格组
     *
     * @param specGroup
     * @return
     */
    @PostMapping("group")
    public ResponseEntity<Void> addSpecGroup(@RequestBody SpecGroup specGroup) {
        if (service.addOrUpdateSpecGroup(specGroup) > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    /**
     * 修改规格组
     *
     * @param specGroup
     * @return
     */
    @PutMapping("group")
    public ResponseEntity<Void> updateSpecGroup(@RequestBody SpecGroup specGroup) {
        if (service.addOrUpdateSpecGroup(specGroup) > 0) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @DeleteMapping("group/{gid}")
    public ResponseEntity<Void> deleteSpecGroup(@PathVariable("gid") Long groupId) {
        if (service.deleteSpecGroup(groupId) > 0) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    /**
     * 查询规格参数
     *
     * @param gid
     * @param cid
     * @param searching
     * @param generic
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParam(@RequestParam(name = "gid", required = false) Long gid,
                                                          @RequestParam(name = "cid", required = false) Long cid,
                                                          @RequestParam(name = "searching", required = false) Boolean searching,
                                                          @RequestParam(name = "generic", required = false) Boolean generic) {
        List<SpecParam> specParams = service.querySpecParam(gid, cid, searching, generic);
        if (specParams == null || specParams.size() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(specParams);
    }

    @PostMapping("param")
    public ResponseEntity<Void> addSpecParam(@RequestBody SpecParam specParam) {
        if (service.addOrUpdateSpecParam(specParam) > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PutMapping("param")
    public ResponseEntity<Void> UpdateSpecParam(@RequestBody SpecParam specParam) {
        if (service.addOrUpdateSpecParam(specParam) > 0) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @DeleteMapping("param/{pid}")
    public ResponseEntity<Void> deleteSpecParam(@PathVariable("pid") Long paramId) {
        if (service.deleteSpecParam(paramId) > 0) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

}
