package fm.douban.app.control;

import fm.douban.model.CollectionViewModel;
import fm.douban.model.Singer;
import fm.douban.model.Song;
import fm.douban.model.Subject;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import fm.douban.util.SubjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SubjectControl {
    @Autowired
    private SingerService singerService;

    @Autowired
    private SongService songService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping(path="/artist")
    public String mhzDetail(Model model, @RequestParam(name="subjectId") String subjectId) {
        // 根据参数获取相应的主题/歌手
        Subject subject = subjectService.get(subjectId);

        // 得到主题/歌手关联的歌曲
        List<String> songIds = subject.getSongIds();
        List<Song> songs = new ArrayList<>();
        if(songIds != null) {
            for(String id : songIds) {
                if(songs.size() >= 5) {
                    break;
                }
                Song song = songService.get(id);
                songs.add(song);
            }
        }


        // 得到主题关联的歌手
        String sid = subject.getMaster();
        Singer singer = singerService.get(sid);
    
        // 查找相似歌手列表
        List<String> simSingersId = singer.getSimilarSingerIds();
        List<Singer> simSingers = new ArrayList<>();

        for(String id: simSingersId) {
            simSingers.add(singerService.get(id));
        }

        model.addAttribute("subject", subject);
        model.addAttribute("songs", songs);
        model.addAttribute("singer", singer);
        model.addAttribute("simSingers", simSingers);

        return "mhzdetail";
    }

    // 歌单列表
    @GetMapping(path="/collection")
    public String collection(Model model) {
        List<Subject> collections = subjectService.getSubjects(SubjectUtil.TYPE_COLLECTION);
        List<Subject> collection = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            collection.add(collections.get(i));
        }

        List<CollectionViewModel> viewModels = new ArrayList<>();
        for(Subject subject: collection) {
            CollectionViewModel viewModel = new CollectionViewModel();

            // 设置歌单id
            viewModel.setId(subject.getId());
            // 设置歌单名称
            viewModel.setName(subject.getName());
            // 设置歌单封面
            viewModel.setCover(subject.getCover());
            List<Song> songs = new ArrayList<>();
            for(String id: subject.getSongIds()) {
                Song song = songService.get(id);
                songs.add(song);
            }
            // 设置相应歌曲列表
            viewModel.setSongs(songs);

            Singer singer = singerService.get(subject.getMaster());

            // 设置创建者名称
            viewModel.setSinger(singer.getName());

            viewModels.add(viewModel);
        }

        model.addAttribute("models", viewModels);

        return "collection";
    }

    // 歌单详情
    @GetMapping(path="/collectiondetail")
    public String collectionDetail(Model model, @RequestParam(name = "subjectId") String subjectId) {
        // 相关主题
        Subject subject = subjectService.get(subjectId);

        // 歌单作者
        String sid = subject.getMaster();
        Singer singer = singerService.get(sid);

        // 歌单歌曲
        List<String> songIds = subject.getSongIds();
        List<Song> songs = new ArrayList<>();
        if(songIds != null) {
            for(String id : songIds) {
                if(songs.size() >= 5) {
                    break;
                }
                Song song = songService.get(id);
                songs.add(song);
            }
        }

        // 该主题作者的其他歌单
        Subject param = new Subject();
        param.setMaster(sid);
        List<Subject> otherSubjectss = subjectService.getSubjects(param);
        List<Subject> otherSubjects = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            otherSubjects.add(otherSubjectss.get(i));
        }

        model.addAttribute("subject", subject);
        model.addAttribute("singer", singer);
        model.addAttribute("songs", songs);
        model.addAttribute("otherSubjects", otherSubjects);

        return "collectiondetail";
    }
}
