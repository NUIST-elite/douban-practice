package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fm.douban.model.Singer;
import fm.douban.model.Subject;
import fm.douban.service.SubjectService;
import fm.douban.util.SubjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {
    private static final Logger LOG = LoggerFactory.getLogger(SubjectServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    // 增加一个主题
    @Override
    public Subject addSubject(Subject subject) {
        if(subject == null) {
            LOG.error("subject data is null");
            return null;
        }

        return mongoTemplate.insert(subject);
    }

    // 查询单个主题
    @Override
    public Subject get(String subjectId) {
        if(!StringUtils.hasText(subjectId)) {
            LOG.error("subjectId is invalid");
            return null;
        }

        return mongoTemplate.findById(subjectId, Subject.class);
    }

    // 查询一组主题
    @Override
    public List<Subject> getSubjects(String type) {
        Subject subjectParam = new Subject();
        subjectParam.setSubjectType(type);

        return getSubjects(subjectParam);
    }

    // 查询一组主题
    @Override
    public List<Subject> getSubjects(String type, String subType) {
        Subject subjectParam = new Subject();
        subjectParam.setSubjectType(type);
        subjectParam.setSubjectSubType(subType);

        return getSubjects(subjectParam);
    }

    // 根据歌手查询主题
    @Override
    public List<Subject> getSubjects(Subject subjectParam) {
        if(subjectParam == null) {
            LOG.error("input subjectParam is not correct.");
            return null;
        }

        Criteria criteria = new Criteria();
        List<Criteria> subCris = new ArrayList<>();


        if(StringUtils.hasText(subjectParam.getSubjectType())) {
            subCris.add(Criteria.where("subjectType").is(subjectParam.getSubjectType()));
        }

        if(StringUtils.hasText(subjectParam.getSubjectSubType())) {
            subCris.add(Criteria.where("subjectSubType").is(subjectParam.getSubjectSubType()));
        }

        if(StringUtils.hasText((subjectParam.getMaster()))) {
            subCris.add(Criteria.where("master").is(subjectParam.getMaster()));
        }

        criteria.andOperator(subCris.toArray(new Criteria[] {}));
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Subject.class);
    }

    // 删除一个主题
    public boolean delete(String subjectId) {
        if(!StringUtils.hasText(subjectId)) {
            LOG.error("subjectId is invalid");
            return false;
        }

        Subject subject = new Subject();
        subject.setId(subjectId);

        DeleteResult result = mongoTemplate.remove(subject);
        return result.getDeletedCount() > 0;
    }

    public boolean modify(Subject subject){
        if(subject == null || !StringUtils.hasText(subject.getId())) {
            LOG.error("subject data is not correct");
            return false;
        }

        Query query = new Query(Criteria.where("id").is(subject.getId()));

        Update updateData = new Update();

        if(subject.getSongIds() != null) {
            updateData.set("songIds", subject.getSongIds());
        }

        UpdateResult result =mongoTemplate.updateFirst(query, updateData, Subject.class);
        return result.getModifiedCount() > 0;
    }
}
