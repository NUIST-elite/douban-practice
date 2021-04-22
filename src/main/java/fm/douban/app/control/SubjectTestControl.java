package fm.douban.app.control;

import fm.douban.model.Subject;
import fm.douban.service.SubjectService;
import fm.douban.util.SubjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubjectTestControl {
    @Autowired
    private SubjectService subjectService;

    @GetMapping(path="/test/subject/add")
    public Subject testAdd() {
        Subject subject = new Subject();
        subject.setId("0");
        subject.setSubjectType(SubjectUtil.TYPE_MHZ);
        subject.setSubjectSubType((SubjectUtil.TYPE_SUB_MOOD));

        return subjectService.addSubject(subject);
    }

    @GetMapping(path="/test/subject/get")
    public Subject testGet() {
        return subjectService.get("23434");
    }

    @GetMapping(path="/test/subject/getByType")
    public List<Subject> testGetByType() {
        return subjectService.getSubjects(SubjectUtil.TYPE_MHZ);
    }

    @GetMapping(path="/test/subject/getBySubType")
    public List<Subject> testGetBySubType() {
        return subjectService.getSubjects(SubjectUtil.TYPE_MHZ, SubjectUtil.TYPE_SUB_STYLE);
    }

    @GetMapping(path="/test/subject/del")
    public boolean testDelete() {
//        List<Subject> subjectOne = subjectService.getSubjects(SubjectUtil.TYPE_MHZ);
//        List<Subject> subjectTwo = subjectService.getSubjects(SubjectUtil.TYPE_COLLECTION);
//        for(Subject subject: subjectOne) {
//            subjectService.delete(subject.getId());
//        }
//        for(Subject subject: subjectTwo) {
//            subjectService.delete(subject.getId());
//        }
        return true;
    }
}
