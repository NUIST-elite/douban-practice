package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import fm.douban.model.Favorite;
import fm.douban.service.FavoriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    private static final Logger LOG = LoggerFactory.getLogger(SingerServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Favorite add(Favorite fav) {
        if(fav == null) {
            LOG.error("input is not correct.");
            return null;
        }

        return mongoTemplate.insert(fav);
    }

    @Override
    public List<Favorite> list(Favorite favParam) {
        if(favParam == null) {
            LOG.error("input data is not correct.");
            return null;
        }

        Criteria criteria = new Criteria();
        List<Criteria> subCris = new ArrayList<>();

        if(StringUtils.hasText(favParam.getId())) {
            subCris.add(Criteria.where("id").is(favParam.getId()));
        }

        if(StringUtils.hasText((favParam.getUserId()))) {
            subCris.add(Criteria.where("userId").is(favParam.getUserId()));
        }

        if(StringUtils.hasText(favParam.getType())) {
            subCris.add(Criteria.where("type").is(favParam.getType()));
        }

        if(StringUtils.hasText(favParam.getItemType())) {
            subCris.add(Criteria.where("itemType").is(favParam.getItemType()));
        }

        if(StringUtils.hasText(favParam.getItemId())) {
            subCris.add(Criteria.where("itemId").is(favParam.getItemId()));
        }

        criteria.andOperator(subCris.toArray(new Criteria[] {}));
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Favorite.class);

    }

    @Override
    public boolean delete(Favorite favParam) {
        if(favParam == null) {
            LOG.error("input is not correct.");
            return false;
        }

        DeleteResult result = mongoTemplate.remove(favParam);
        return result.getDeletedCount() > 0;
    }
}
