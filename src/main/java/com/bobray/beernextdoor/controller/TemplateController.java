package com.bobray.beernextdoor.controller;

import com.bobray.beernextdoor.entity.*;
import com.bobray.beernextdoor.repository.*;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class TemplateController {

    //Repos
    @Autowired
    TypeRepository typeRepository;

    @Autowired
    BreweryRepository breweryRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${spring.datasource.salty}")
    private String salt;

    //Mappings
    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/log")
    public String getLog() {
        return "log";
    }

    @GetMapping("/fragment")
    public String getFragments() {
        return "fragment";
    }

    @GetMapping("/sign")
    public String getSign(Model out,
                          @ModelAttribute User user) {
        out.addAttribute("user", user);
        return "sign";
    }

    @PostMapping("/connexion")
    public String connexion(HttpServletResponse response,
                            HttpSession session,
                            @RequestParam String nameUser,
                            @RequestParam String password) {

        Optional<User> userOptional = userRepository.findByNameUser(nameUser);
        Optional<User> userOptionalMail = userRepository.findByEmail(nameUser);
        String sha256hex = Hashing.sha256()
                .hashString(salt + password, StandardCharsets.UTF_8)
                .toString();

        if (userOptional.isPresent() || userOptionalMail.isPresent()) {
            User user;
            if (userOptional.isPresent()) {
                user = userOptional.get();
            } else {
                user = userOptionalMail.get();
            }
            if (sha256hex.equals(user.getPassword())) {
                user = getConnected(user, response);
                session.setAttribute("user", user);
                return "redirect:/type-form";
            }
        }
        return "log";
    }

    @PostMapping("sign-in")
    public String signIn(Model out,
                         HttpServletResponse response,
                         HttpSession session,
                         @ModelAttribute User user,
                         @RequestParam String password,
                         @RequestParam String confirmPass) {
        out.addAttribute("user", user);

        if (user.getNameUser() != null) {
            Optional<User> userOptional = userRepository.findByNameUser(user.getNameUser());
            if (userOptional.isPresent()) {
                return "redirect:/sign";
            }

            userOptional = userRepository.findByEmail(user.getEmail());
            if (userOptional.isPresent()) {
                return "redirect:/sign";
            }

            if (user.getNameUser().trim().equals("")) {
                return "redirect:/sign";
            }

            if (!user.getPassword().equals(confirmPass)) {
                return "redirect:/sign";
            }
            user.setApiKey(RandomStringUtils.randomAlphanumeric(20));
            user.setPassword(Hashing.sha256()
                    .hashString(salt + password, StandardCharsets.UTF_8)
                    .toString());
            user = getConnected(user, response);
            session.setAttribute("user", user);
        }
        return "redirect:/type-form";
    }

    @GetMapping("/user-profile")
    public String getProfile(Model out,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        out.addAttribute("user", user);

        return "user-profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(Model out,
                                HttpSession session,
                                @RequestParam String nameUser,
                                @RequestParam String email,
                                @RequestParam String password,
                                @RequestParam String confirmPass) {

        User user = (User) session.getAttribute("user");
        out.addAttribute("user", user);

        //TODO gestion de l'erreur
        if (!password.equals("") && password.equals(confirmPass)) {
            user.setPassword(Hashing.sha256()
                    .hashString(salt + password, StandardCharsets.UTF_8)
                    .toString());
        }

        if (!nameUser.equals(user.getNameUser())) {
            user.setNameUser(nameUser);
        }

        if (!email.equals(user.getEmail())) {
            user.setEmail(email);
        }
        userRepository.saveAndFlush(user);
        session.setAttribute("user", user);
        return "redirect:/user-profile";
    }

    @GetMapping("/type-form")
    public String getTypeForm(Model out,
                              HttpSession session,
                              @ModelAttribute Type newType) {

        if (sessionCheck(session)) {
            return "redirect:/log";
        }

        List<Type> types = typeRepository.findAll();
        out.addAttribute("types", types);
        out.addAttribute("newType", newType);
        return "type-form";
    }

    @PostMapping("/post-type")
    public String postNewType(Model out,
                              @ModelAttribute Type newType,
                              @RequestParam(required = false, defaultValue = "") String chooseType,
                              @RequestParam(required = false) Long typeId) {

        List<Type> types = typeRepository.findAll();
        out.addAttribute("types", types);

        if (chooseType.equals("Choisir")) {
            Optional<Type> typeOptional = typeRepository.findById(typeId);
            if (typeOptional.isPresent()) {
                newType = typeOptional.get();
                out.addAttribute("newType", newType);
            } else {
                out.addAttribute("newType", new Type());
            }
            return "type-form";
        }
        if (chooseType.equals("Go !")) {
            if (newType.getNameType() != null && !newType.getNameType().equals("")) {
                if (typeRepository.findByNameType(newType.getNameType()).isPresent()) {
                    return "redirect:/type-form";
                }
                typeRepository.save(newType);
                return "redirect:/type-form";
            }
        }
        return "redirect:/type-form";
    }

    @GetMapping("/brewery-form")
    public String getBreweryForm(Model out,
                                 HttpSession session) {

        if (sessionCheck(session)) {
            return "redirect:/log";
        }

        List<Brewery> breweries = breweryRepository.findAll();
        out.addAttribute("breweries", breweries);
        out.addAttribute("newBrewery", new Brewery());
        return "brewery-form";
    }

    @PostMapping("/post-brewery")
    public String postNewBrewery(Model out,
                                 @ModelAttribute Brewery newBrewery) {
        out.addAttribute("newBrewery", newBrewery);
        List<Brewery> breweries = breweryRepository.findAll();
        out.addAttribute("breweries", breweries);

        if (newBrewery.getNameBrewery() != null) {

            if (breweryRepository.findByNameBrewery(newBrewery.getNameBrewery()).isPresent()) {
                //TODO update
                return "redirect:/brewery-form";
            }
            breweryRepository.save(newBrewery);
        }
        return "redirect:/brewery-form";
    }

    @GetMapping("/beer-form")
    public String getBeerForm(Model out,
                              HttpSession session) {

        if (sessionCheck(session)) {
            return "redirect:/log";
        }

        List<Beer> beers = beerRepository.findAll();
        List<Type> types = typeRepository.findAll();
        List<Brewery> breweries = breweryRepository.findAll();
        out.addAttribute("beers", beers);
        out.addAttribute("newBeer", new Beer());
        out.addAttribute("types", types);
        out.addAttribute("breweries", breweries);
        return "beer-form";
    }

    @PostMapping("/post-beer")
    public String postNewBeer(Model out,
                              @ModelAttribute Beer newBeer) {

        out.addAttribute("newBeer", newBeer);
        List<Beer> beers = beerRepository.findAll();
        out.addAttribute("beers", beers);

        if (newBeer.getNameBeer() != null) {

            if (beerRepository.findByNameBeer(newBeer.getNameBeer()).isPresent()) {
                return "redirect:/beer-form";
            }

            Brewery currentBrewery = breweryRepository.findById(newBeer.getBrewery().getIdBrewery()).get();
            newBeer.setBrewery(currentBrewery);

            Type currentType = typeRepository.findById(newBeer.getType().getIdType()).get();
            newBeer.setType(currentType);

            beerRepository.save(newBeer);
        }
        return "redirect:/beer-form";
    }

    @GetMapping("/store-form")
    public String getStoreForm(Model out,
                               HttpSession session) {

        if (sessionCheck(session)) {
            return "redirect:/log";
        }

        List<Store> stores = storeRepository.findAll();
        out.addAttribute("stores", stores);
        out.addAttribute("newStore", new Store());
        return "store-form";
    }

    @PostMapping("/post-store")
    public String postNewStore(Model out,
                               @ModelAttribute Store newStore) {

        out.addAttribute("newStore", newStore);
        List<Store> stores = storeRepository.findAll();
        out.addAttribute("stores", stores);

        if (newStore.getNameStore() != null) {

            if (storeRepository.findByNameStore(newStore.getNameStore()).isPresent()) {
                return "redirect:/store-form";
            }
            storeRepository.save(newStore);
        }
        return "redirect:/store-form";
    }

    @GetMapping("/out")
    public String getOut(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    //Methods
    public User getConnected(User user,
                             HttpServletResponse response) {
        String sessionToken = RandomStringUtils.randomAlphanumeric(30);
        user.setToken(sessionToken);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, 1);
        user.setTokenExpiration(c.getTime());
        Cookie tokenSession = new Cookie("sessionId", user.getToken());
        tokenSession.setMaxAge(24 * 60 * 60);
        response.addCookie(tokenSession);
        userRepository.save(user);
        return user;
    }

    public boolean sessionCheck(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user == null;
    }
}