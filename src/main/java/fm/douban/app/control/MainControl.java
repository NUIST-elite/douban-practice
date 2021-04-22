package fm.douban.app.control;

import fm.douban.model.*;
import fm.douban.param.SongQueryParam;
import fm.douban.service.FavoriteService;
import fm.douban.service.SingerService;
import fm.douban.service.SongService;
import fm.douban.service.SubjectService;
import fm.douban.util.FavoriteUtil;
import fm.douban.util.SubjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainControl {

    private static final Logger LOG = LoggerFactory.getLogger(MainControl.class);
    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SongService songService;

    @Autowired
    private SingerService singerService;

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping(path="/index")
    public String index(Model model) {
        SongQueryParam songParam = new SongQueryParam();
        // 取得一页数据，每页一条数据
        songParam.setPageNum(1);
        songParam.setPageSize(1);

        Page<Song> songs = songService.list(songParam);
        Song song = songs.getContent().get(0);
        model.addAttribute("song", song);

        List<String> singerIds = song.getSingerId();
        List<Singer> singers = new ArrayList<>();
        for (String singerId : singerIds) {
            Singer singer = singerService.get(singerId);
            singers.add(singer);
        }
        model.addAttribute("singers", singers);
        Singer singer = singers.get(0);
        model.addAttribute("singer", singer);

        List<Subject> artistDatas = new ArrayList<>();
        List<Subject> moodSubjects = new ArrayList<>();
        List<Subject> ageSubjects = new ArrayList<>();
        List<Subject> styleSubjects = new ArrayList<>();
        List<MhzViewModel> mhzViewModels = new ArrayList<>();
        List<Subject> allSubjects = subjectService.getSubjects(SubjectUtil.TYPE_MHZ);
        for (Subject subject : allSubjects) {
            if (subject.getSubjectSubType().equals(SubjectUtil.TYPE_SUB_ARTIST)) {
                if(artistDatas.size() == 10) {
                    continue;
                }
                artistDatas.add(subject);
            } else if (subject.getSubjectSubType().equals(SubjectUtil.TYPE_SUB_MOOD)) {
                moodSubjects.add(subject);
            } else if (subject.getSubjectSubType().equals(SubjectUtil.TYPE_SUB_AGE)) {
                ageSubjects.add(subject);
            } else if (subject.getSubjectSubType().equals(SubjectUtil.TYPE_SUB_STYLE)) {
                styleSubjects.add(subject);
            }
        }

        MhzViewModel moodViewModels = new MhzViewModel();
        MhzViewModel ageViewModels = new MhzViewModel();
        MhzViewModel styleViewModels = new MhzViewModel();

        moodViewModels.setTitle("心情 / 场景");
        ageViewModels.setTitle("语言 / 年代");
        styleViewModels.setTitle("风格 / 流派");

        moodViewModels.setSubjects(moodSubjects);
        ageViewModels.setSubjects(ageSubjects);
        styleViewModels.setSubjects(styleSubjects);

        mhzViewModels.add(moodViewModels);
        mhzViewModels.add(ageViewModels);
        mhzViewModels.add(styleViewModels);

        model.addAttribute("artistDatas", artistDatas);
        model.addAttribute("mhzViewModels", mhzViewModels);

        return "index";
    }

    @GetMapping(path="/search")
    public String search(Model model) {
        return "search";
    }

    @GetMapping(path="/searchContent")
    @ResponseBody
    public Map searchContent(@RequestParam(name="keyword") String keyword) {
        SongQueryParam queryParam = new SongQueryParam();
        queryParam.setName(keyword);
        Page<Song> songs = songService.list(queryParam);
        Map<String, Page<Song>> songMap = new HashMap<>();
        songMap.put("songs", songs);
        return songMap;
    }

    @GetMapping(path = "/my")
    public String myPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserLoginInfo info = (UserLoginInfo) session.getAttribute("userLoginInfo");
        String userId = info.getUserId();

        Favorite fav = new Favorite();
        fav.setUserId(userId);
        List<Favorite> favorites = favoriteService.list(fav);

        List<Song> songs = new ArrayList<>();
        for(Favorite favorite: favorites) {
            if(favorite.getItemType().equals(FavoriteUtil.ITEM_TYPE_SONG)) {
                String songId = favorite.getItemId();
                Song song = songService.get(songId);
                songs.add(song);
            }
        }

        model.addAttribute("favorites", favorites);
        model.addAttribute("songs", songs);
        return "my";
        
    }

    @GetMapping(path = "/fav")
    @ResponseBody
    public Map doFav(@RequestParam(name = "itmeType") String itemType, @RequestParam(name = "itemId") String itemId) {
        Map returnData = new HashMap();
        if(!StringUtils.hasText(itemType) || !StringUtils.hasText(itemId)) {
            LOG.error("input data is not valid.");
            returnData.put("message", "failed");
            return returnData;
        }

        Favorite favorite = new Favorite();
        favorite.setItemId(itemId);
        favorite.setItemType(itemType);

        List<Favorite> list = favoriteService.list(favorite);
        if(list == null) {
            favoriteService.add(favorite);
        } else {
            favoriteService.delete(favorite);
        }

        returnData.put("message", "successful");
        return returnData;
    }
}
