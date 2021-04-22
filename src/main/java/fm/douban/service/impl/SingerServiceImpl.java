package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fm.douban.model.Singer;
import fm.douban.model.Subject;
import fm.douban.service.SingerService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

@Service
public class SingerServiceImpl implements SingerService {
    private static final Logger LOG =LoggerFactory.getLogger(SingerServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    // 增加一个歌手
    @Override
    public Singer addSinger(Singer singer) {
        if(singer == null) {
            LOG.error("singer data is null");
            return null;
        }

        return mongoTemplate.insert(singer);
    }

    // 根据歌手id查询歌手
    @Override
    public Singer get(String singerId){
        if(!StringUtils.hasText(singerId)) {
            LOG.error("singerId is invalid");
            return null;
        }

        return mongoTemplate.findById(singerId, Singer.class);
    }

    // 查询全部歌手
    public List<Singer> getAll(){
        return mongoTemplate.findAll(Singer.class);
    }

    // 修改歌手。只能修改名称，头像，主页，相似的歌手id
    public boolean modify(Singer singer){
        if(singer == null || !StringUtils.hasText(singer.getId())) {
            LOG.error("singer data is not correct");
            return false;
        }

        Query query = new Query(Criteria.where("id").is(singer.getId()));

        Update updateData = new Update();

        if(singer.getName() != null) {
            updateData.set("name", singer.getName());
        }

        if(singer.getAvatar() != null) {
            updateData.set("avatar", singer.getAvatar());
        }

        if(singer.getHomepage() != null) {
            updateData.set("homepage", singer.getHomepage());
        }

        if(singer.getSimilarSingerIds() != null) {
            updateData.set("similarSingerIds", singer.getSimilarSingerIds());
        }

        UpdateResult result =mongoTemplate.updateFirst(query, updateData, Singer.class);
        return result.getModifiedCount() > 0;
    }

    // 根据id主键删除歌手
    public boolean delete(String singerId){
        if(!StringUtils.hasText(singerId)) {
            LOG.error("singerId is invalid");
        }

        Singer singer = new Singer();
        singer.setId(singerId);

        DeleteResult result = mongoTemplate.remove(singer);
        return result.getDeletedCount() > 0;
    }

    public List<Singer> getByName(String name) {
        Criteria criteria = Criteria.where("name").is(name);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Singer.class);
    }
}
