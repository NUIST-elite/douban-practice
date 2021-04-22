package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fm.douban.model.Song;
import fm.douban.param.SongQueryParam;
import fm.douban.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

@Service
public class SongServiceImpl implements SongService {
    private static final Logger LOG = LoggerFactory.getLogger(SingerServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    // 增加一个歌曲
    @Override
    public Song add(Song song) {
        if(song == null) {
            LOG.error("song data is null");
            return null;
        }

        return mongoTemplate.insert(song);
    }

    // 根据歌曲id查询歌曲
    @Override
    public Song get(String songId){
        if(!StringUtils.hasText(songId)) {
            LOG.error("songId is invalid");
            return null;
        }

        return mongoTemplate.findById(songId, Song.class);
    }

    // 查询全部歌曲
    @Override
    public Page<Song> list(SongQueryParam songParam) {
        if(songParam == null) {
            LOG.error("songParam is empty");
            return null;
        }

        Criteria criteria = new Criteria();
        List<Criteria> subCris = new ArrayList<>();

        if(StringUtils.hasText(songParam.getName())) {
            subCris.add(Criteria.where("name").is(songParam.getName()));
        }

        if(StringUtils.hasText(songParam.getLyrics())) {
            subCris.add(Criteria.where("lyrics").is(songParam.getLyrics()));
        }

        Query query;
        if(subCris.isEmpty()) {
            query = new Query();
            long count = mongoTemplate.count(query, Song.class);

            Pageable pageable = PageRequest.of(songParam.getPageNum()-1, songParam.getPageSize());
            query.with(pageable);

            // 歌曲查询结果
            List<Song> songs = mongoTemplate.findAll(Song.class);
            Page<Song> pageResult = PageableExecutionUtils.getPage(songs, pageable, new LongSupplier() {
                @Override
                public long getAsLong() {
                    return count;
                }
            });

            return pageResult;
        }

        criteria.andOperator(subCris.toArray(new Criteria[] {}));
        query = new Query(criteria);
        long count = mongoTemplate.count(query, Song.class);

        Pageable pageable = PageRequest.of(songParam.getPageNum()-1, songParam.getPageSize());
        query.with(pageable);

        // 歌曲查询结果
        List<Song> songs = mongoTemplate.find(query, Song.class);
        Page<Song> pageResult = PageableExecutionUtils.getPage(songs, pageable, new LongSupplier() {
            @Override
            public long getAsLong() {
                return count;
            }
        });

        return pageResult;
    }

    // 修改一首歌
    @Override
    public boolean modify(Song song) {
        if(song == null || !StringUtils.hasText(song.getId())) {
            LOG.error("song data is not correct");
            return false;
        }

        Query query = new Query(Criteria.where("id").is(song.getId()));

        Update updateData = new Update();

        if(song.getName() != null) {
            updateData.set("name", song.getName());
        }

        if(song.getLyrics() != null) {
            updateData.set("lyrics", song.getLyrics());
        }

        if(song.getCover() != null) {
            updateData.set("cover", song.getCover());
        }

        if(song.getUrl() != null) {
            updateData.set("url", song.getUrl());
        }

        if(song.getSingerId() != null) {
            updateData.set("singerId", song.getSingerId());
        }

        UpdateResult result =mongoTemplate.updateFirst(query, updateData, Song.class);
        return result.getModifiedCount() > 0;
    }

    // 删除一首歌
    @Override
    public boolean delete(String songId) {
        if(!StringUtils.hasText(songId)) {
            LOG.error("songId is invalid");
        }

        Song song = new Song();
        song.setId(songId);

        DeleteResult result = mongoTemplate.remove(song);
        return result.getDeletedCount() > 0;
    }
}
