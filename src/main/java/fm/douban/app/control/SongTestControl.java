package fm.douban.app.control;

import fm.douban.model.Song;
import fm.douban.param.SongQueryParam;
import fm.douban.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SongTestControl {
    @Autowired
    private SongService songService;

    @GetMapping(path="/test/song/add")
    public Song testAdd() {
        Song song = new Song();
        song.setId("0");
        return songService.add(song);
    }

    @GetMapping(path="/test/song/get")
    public Song testGet() {
        return songService.get("384720");
    }

    @GetMapping(path="/test/song/list")
    public Page<Song> testList() {
        SongQueryParam songParam = new SongQueryParam();
        songParam.setPageSize(1);
        return songService.list(songParam);
    }

    @GetMapping(path="/test/song/modify")
    public boolean testModify() {
        Song song = new Song();
        song.setId("0");
        song.setName("test");
        return songService.modify(song);
    }

    @GetMapping(path="/test/song/del")
    public boolean testDelete() {
        return songService.delete("0");
    }
}
