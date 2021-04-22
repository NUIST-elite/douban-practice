package fm.douban.app.control;

import fm.douban.model.Singer;
import fm.douban.service.SingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class SingerControl {
    @Autowired
    private SingerService singerService;

    @GetMapping(path = "/user-guide")
    public String myMhz(Model model) {
        List<Singer> tenSingers = this.randomSingers();
        model.addAttribute("singers", tenSingers);
        return "userguide";
    }

    @ResponseBody
    @GetMapping(path = "/singer/random")
    public List<Singer> randomSingers() {
        List<Singer> allSingers = singerService.getAll();
        List<Singer> randomTenSingers = new ArrayList<>();
        Random random = new Random();
        for(int i = 1; i <= 10; i++) {
            Singer singer = allSingers.get(random.nextInt(allSingers.size()-1));
            randomTenSingers.add(singer);
        }

        return randomTenSingers;
    }
}
