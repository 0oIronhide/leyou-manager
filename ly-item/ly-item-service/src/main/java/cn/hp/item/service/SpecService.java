package cn.hp.item.service;

import cn.hp.item.mapper.SpecGroupMapper;
import cn.hp.item.mapper.SpecParamMapper;
import cn.hp.item.pojo.SpecGroup;
import cn.hp.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Ironhide
 * @create 2020-04-30-10:18
 */
@Service
public class SpecService {

    @Autowired
    private SpecGroupMapper groupMapper;

    @Autowired
    private SpecParamMapper paramMapper;

    public List<SpecGroup> querySpecGroup(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroups = groupMapper.select(specGroup);
        specGroups.forEach(group -> {
            SpecParam specParam = new SpecParam();
            specParam.setGroupId(specGroup.getId());
            group.setSpecParams(paramMapper.select(specParam));
        });
        return specGroups;
    }

    @Transactional
    public Integer addOrUpdateSpecGroup(SpecGroup specGroup) {
        if (specGroup.getId() == null) {
            return groupMapper.insertSelective(specGroup);
        } else {
            return groupMapper.updateByPrimaryKey(specGroup);
        }
    }

    @Transactional
    public Integer deleteSpecGroup(Long groupId) {
        paramMapper.deleteGroupParam(groupId);
        return groupMapper.deleteByPrimaryKey(groupId);
    }

    public List<SpecParam> querySpecParam(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);
        return paramMapper.select(specParam);
    }

    @Transactional
    public Integer addOrUpdateSpecParam(SpecParam specParam) {
        if (specParam.getId() == null) {
            return paramMapper.insertSelective(specParam);
        } else {
            return paramMapper.updateByPrimaryKeySelective(specParam);
        }
    }

    @Transactional
    public Integer deleteSpecParam(Long paramId) {
        return paramMapper.deleteByPrimaryKey(paramId);
    }

}
