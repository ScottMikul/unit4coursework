import com.teamtreehouse.courses.model.CourseIdea;
import com.teamtreehouse.courses.model.CourseIdeaDao;
import com.teamtreehouse.courses.model.NotFoundException;
import com.teamtreehouse.courses.model.SimpleCourseIdeaDao;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.BasicConfigurator;
import spark.ModelAndView;

import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static spark.Spark.*;

/**
 * Created by scott on 4/5/2017.
 */
public class main {
    private static final String FLASH_MESSAGE_KEY = "flash_message";

    public static void main(String[] args) {
        BasicConfigurator.configure();
        staticFileLocation("/public");

        CourseIdeaDao dao = new SimpleCourseIdeaDao();

        before((req,res)->{
            if(req.cookie("username")!=null){
                req.attribute("username",req.cookie("username"));
            }
        });

        before("/ideas", (req,res)->{
            if(req.attribute("username")==null){
                setFlashMessage(req,"Whoops please sign in first!");
                res.redirect("/");
                halt();
            }
        });
        get("/", (req, res) -> {
           Map <String, String> model = new HashMap<>();
            model.put("username",req.attribute("username"));
            model.put("flashMessage",captureFlashMessage(req));
            return new ModelAndView(model,"index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/sign-in", (req,res)->{
            Map <String, String> model = new HashMap<>();
            String username = req.queryParams("username");
            res.cookie("username",username);
            model.put("username",username);
            return new ModelAndView(model,"sign-in.hbs");
            }, new HandlebarsTemplateEngine()
        );

        get("/ideas", (req, res) ->{
            Map <String,Object > model= new HashMap<>();
            model.put("ideas", dao.findAll());
            model.put("flashMessage",captureFlashMessage(req));
            return new ModelAndView(model,"ideas.hbs");
        }, new HandlebarsTemplateEngine());

        get("/ideas/:slug", (req,res)->
        {
            Map <String,Object> model = new HashMap<>();
            CourseIdea idea = dao.findBySlug(req.params(":slug"));

            model.put("idea", idea);
            return new ModelAndView(model,"details.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas",(req,res)->{
            String title = req.queryParams("title");
            CourseIdea courseIdea = new CourseIdea(title,req.attribute("username"));
            dao.add(courseIdea);
            res.redirect("/ideas");
            return null;

        });

        post("/ideas/:slug/vote", (req,res)->{
            CourseIdea idea = dao.findBySlug(req.params("Slug"));
            boolean added = idea.addVoter(req.attribute("username"));
            if(added){
                setFlashMessage(req,"Thanks for your vote");
            }else {
                setFlashMessage(req,"You already voted!");
            }
            res.redirect("/ideas");
            return null;
        });

        exception(NotFoundException.class,(exec, req, res)->{
            res.status(404);
            HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
            String html = engine.render(new ModelAndView(null, "not-found.hbs"));
            res.body(html);
        });

    }



    private static void setFlashMessage(Request req, String message) {
        req.session().attribute(FLASH_MESSAGE_KEY,message);
    }

    private static String getFlashMessage (Request req){
        if(req.session(false)==null){
            return null;
        }
        if(!req.session().attributes().contains(FLASH_MESSAGE_KEY)){
            return null;
        }
        return (String) req.session().attribute(FLASH_MESSAGE_KEY);

    }
    private static String captureFlashMessage(Request req) {
        String message = getFlashMessage(req);
        if(message!=null){
            req.session().removeAttribute(FLASH_MESSAGE_KEY);
        }
        return message;
    }

}
