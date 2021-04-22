package fm.douban.model;

import java.time.LocalDateTime;
import java.util.List;

public class Subject {
    // 主键id
    private String id;

    // 创建时间
    private LocalDateTime gmtCreated;

    // 修改时间
    private LocalDateTime gmtModified;

    // 名称
    private String name;

    // 详细说明
    private String description;

    // 封面图
    private String cover;

    // 对歌单（collection）来说，指作家
    // 对兆赫来说（mhz）来说，指音乐家
    private String master;

    // 发布时间
    private LocalDateTime publishedDate;

    // 主题的一级分类，兆赫（mhz）、歌单（collection）
    private String subjectType;

    // 主题的二级分类
    // 兆赫（mhz）下的细分分类：artist（从艺术家出发）、mood（心情/场景）、age（语言）、style（风格/流派）
    private String subjectSubType;

    // 关联的歌曲列表
    private List<String> songIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(LocalDateTime gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getSubjectSubType() {
        return subjectSubType;
    }

    public void setSubjectSubType(String subjectSubType) {
        this.subjectSubType = subjectSubType;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<String> songIds) {
        this.songIds = songIds;
    }
}
