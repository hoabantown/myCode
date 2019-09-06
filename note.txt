For you to call your service methods from your Thymeleaf template you need to add that service into your model like so

@Controller
public class PageController {

    @Autowired
    LicenseService licenseService;

    @RequestMapping("/yourPage")
    public String getYourPage(Model model) {
        model.add("licenseService", licenseService);
        return "yourPage.html";
    }

}
