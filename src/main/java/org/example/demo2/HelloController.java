package org.example.demo2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.example.demo2.controller.*;
import org.example.demo2.model.Post;
import org.example.demo2.model.Subreddit;
import org.example.demo2.model.Utilisateur;
import org.example.demo2.service.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



public class HelloController implements Initializable, 
                                         NavBarController.LoginCallback, 
                                         ProfileController.ProfileCallback {

    @FXML
    private HBox navbarContainer;

    @FXML
    private HBox mainContentContainer;

    @FXML
    private TextField postTitleField;

    @FXML
    private TextArea postContentArea;

    @FXML
    private ComboBox<Subreddit> subredditComboBox;

    @FXML
    private HBox allFeedBox;

    @FXML
    private VBox subredditsContainer;

    @FXML
    private VBox trendingContainer;

    @FXML
    private Label postingToLabel;

    @FXML
    private VBox postsContainer;

    private final PostService postService = new PostService();
    private final CommentaireService commentaireService = new CommentaireService();
    private final SubredditService subredditService = new SubredditService();
    private final SignalementService signalementService = new SignalementService();
    private final AbonnementService abonnementService = new AbonnementService();
    private final UtilisateurService utilisateurService = new UtilisateurService();

    private final NavBarController navbarController = new NavBarController();
    private PostCardController postCardController;
    private SubredditSidebarController sidebarController;
    private FeedController feedController;
    private TrendingController trendingController;
    private CreatePostController createPostController;

    private int utilisateurConnecteId = 1;
    private Subreddit savedSubredditActuel = null;
    private List<javafx.scene.Node> savedMainContentChildren = null;
    private List<javafx.scene.Node> originalMainContentChildren = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeControllers();
        initializeNavbar();
        initializeSidebar();
        loadInitialContent();
        saveOriginalMainView();
    }
    
    private void initializeControllers() {
        postCardController = new PostCardController(
            postService, commentaireService, signalementService, utilisateurService
        );
        postCardController.setUtilisateurConnecteId(utilisateurConnecteId);
        postCardController.setOnPostClick(this::navigateToPostDetail);
        
        sidebarController = new SubredditSidebarController(subredditService, abonnementService);
        sidebarController.setUtilisateurConnecteId(utilisateurConnecteId);
        sidebarController.setOnSubredditSelected(this::handleSubredditSelected);
        sidebarController.setOnAllFeedSelected(() -> handleAllFeedClick(null));
        
        feedController = new FeedController(
            postService, abonnementService, utilisateurService, postCardController
        );
        feedController.setUtilisateurConnecteId(utilisateurConnecteId);
        feedController.setOnSubredditChanged(() -> {
            sidebarController.chargerSubreddits();
        });
        
        trendingController = new TrendingController(postService, utilisateurService);
        trendingController.setUtilisateurConnecteId(utilisateurConnecteId);
        trendingController.setOnPostClick(this::navigateToPostDetail);
        
        createPostController = new CreatePostController(postService, subredditService, utilisateurService);
        createPostController.setUtilisateurConnecteId(utilisateurConnecteId);
        createPostController.setOnPostCreated(() -> {
            javafx.application.Platform.runLater(() -> {
                feedController.chargerPosts();
                trendingController.chargerTrendingPosts();
                sidebarController.chargerSubreddits();
            });
        });
    }
    
    private void initializeNavbar() {
        if (navbarContainer != null) {
            String logoPath = getClass().getResource("/org/example/demo2/img/nova_logo_new.png").toExternalForm();
            HBox navbar = navbarController.generateNavbar(logoPath, utilisateurService, this);
            navbarContainer.getChildren().add(navbar);
        }
    }
    
    private void initializeSidebar() {
        sidebarController.setAllFeedBox(allFeedBox);
        sidebarController.setSubredditsContainer(subredditsContainer);
    }
    
    private void loadInitialContent() {
        feedController.setPostsContainer(postsContainer);
        feedController.setSubredditComboBox(subredditComboBox);
        feedController.setPostingToLabel(postingToLabel);
        
        trendingController.setTrendingContainer(trendingContainer);
        
        createPostController.setPostTitleField(postTitleField);
        createPostController.setPostContentArea(postContentArea);
        createPostController.setSubredditComboBox(subredditComboBox);
        
        sidebarController.chargerSubreddits();
        feedController.chargerPosts();
        trendingController.chargerTrendingPosts();
        
        sidebarController.highlightAllFeed();
    }
    
    private void saveOriginalMainView() {
        javafx.application.Platform.runLater(() -> {
            if (originalMainContentChildren == null) {
                originalMainContentChildren = new ArrayList<>(mainContentContainer.getChildren());
            }
        });
    }

    @FXML
    private void handleAllFeedClick(javafx.scene.input.MouseEvent event) {
        if (originalMainContentChildren != null && !originalMainContentChildren.isEmpty()) {
            mainContentContainer.getChildren().clear();
            mainContentContainer.getChildren().addAll(originalMainContentChildren);
        }
        
        savedMainContentChildren = null;
        savedSubredditActuel = null;
        
        feedController.clearSubredditActuel();
        trendingController.clearCurrentSubreddit();
        
        sidebarController.chargerSubreddits();
        feedController.chargerPosts();
        trendingController.chargerTrendingPosts();
        sidebarController.highlightAllFeed();
    }
    
    private void handleSubredditSelected(Subreddit subreddit) {
        feedController.setSubredditActuel(subreddit);
        trendingController.setCurrentSubredditId(subreddit.getId_subreddit());
        
        feedController.chargerPosts();
        trendingController.chargerTrendingPosts();
    }
    
    @FXML
    private void handleCreatePost() {
        createPostController.setSelectedSubreddit(feedController.getSubredditActuel());
        
        createPostController.setOnLoginRequired(() -> {
            navbarController.showLoginDialogWithCallback(() -> {
                createPostController.handleCreatePost();
            });
        });
        
        createPostController.handleCreatePost();
    }

    
    @Override
    public void onLoginSuccess(Utilisateur user) {
        updateConnectedUser(user.getId());
        System.out.println("Bienvenue " + user.getPseudo() + " (ID: " + user.getId() + ")");
    }

    @Override
    public void onLogout() {
        updateConnectedUser(1);
        System.out.println("Déconnexion réussie");
    }

    @Override
    public void onRegisterSuccess(Utilisateur user) {
        updateConnectedUser(user.getId());
        System.out.println("Compte cree! Bienvenue " + user.getPseudo() + " (ID: " + user.getId() + ")");
    }
    
    private void updateConnectedUser(int userId) {
        this.utilisateurConnecteId = userId;
        postCardController.setUtilisateurConnecteId(userId);
        sidebarController.setUtilisateurConnecteId(userId);
        feedController.setUtilisateurConnecteId(userId);
        trendingController.setUtilisateurConnecteId(userId);
        createPostController.setUtilisateurConnecteId(userId);
        
        sidebarController.chargerSubreddits();
        feedController.chargerPosts();
        trendingController.chargerTrendingPosts();
    }

    
    @Override
    public void onProfileClick() {
        if (!utilisateurService.estAuthentifie()) {
            return;
        }
        
        Utilisateur user = utilisateurService.getUtilisateurConnecte();
        navigateToProfile(user);
    }

    private void navigateToProfile(Utilisateur user) {
        if (savedMainContentChildren == null || savedMainContentChildren.isEmpty()) {
            savedMainContentChildren = new ArrayList<>(mainContentContainer.getChildren());
            savedSubredditActuel = feedController.getSubredditActuel();
        }
        
        ProfileController profileController = new ProfileController(user, this);
        VBox profileView = profileController.generateView();
        
        VBox sidebar = (VBox) savedMainContentChildren.get(0);
        
        mainContentContainer.getChildren().clear();
        mainContentContainer.getChildren().addAll(sidebar, profileView);
        HBox.setHgrow(mainContentContainer.getChildren().get(1), Priority.ALWAYS);
    }

    @Override
    public void onBackToHome() {
        if (savedMainContentChildren != null && !savedMainContentChildren.isEmpty()) {
            mainContentContainer.getChildren().clear();
            mainContentContainer.getChildren().addAll(savedMainContentChildren);
            savedMainContentChildren = null;
            savedSubredditActuel = null;
        }
        
        feedController.clearSubredditActuel();
        trendingController.clearCurrentSubreddit();
        
        sidebarController.chargerSubreddits();
        feedController.chargerPosts();
        trendingController.chargerTrendingPosts();
        sidebarController.highlightAllFeed();
    }

    @Override
    public void onPostClick(Post post) {
        navigateToPostDetail(post);
    }

    private void navigateToPostDetail(Post post) {
        savedSubredditActuel = feedController.getSubredditActuel();
        savedMainContentChildren = new ArrayList<>(mainContentContainer.getChildren());
        
        PostDetailController detailController = new PostDetailController(post, new PostDetailController.PostDetailCallback() {
            @Override
            public void onBackToHome() {
                mainContentContainer.getChildren().clear();
                mainContentContainer.getChildren().addAll(savedMainContentChildren);
                feedController.setSubredditActuel(savedSubredditActuel);
                feedController.chargerPosts();
                trendingController.chargerTrendingPosts();
            }

            @Override
            public void onLoginSuccess(Utilisateur user) {
                updateConnectedUser(user.getId());
            }

            @Override
            public void onLogout() {
                updateConnectedUser(1);
            }

            @Override
            public void onRegisterSuccess(Utilisateur user) {
                updateConnectedUser(user.getId());
            }
        });
        detailController.setUtilisateurConnecteId(utilisateurConnecteId);
        
        VBox detailView = detailController.generateView();
        HBox detailHBox = (HBox) detailView.getChildren().get(0);
        VBox postDetail = (VBox) detailHBox.getChildren().get(0);
        VBox relatedPosts = (VBox) detailHBox.getChildren().get(1);
        
        VBox sidebar = (VBox) savedMainContentChildren.get(0);
        
        mainContentContainer.getChildren().clear();
        mainContentContainer.getChildren().addAll(sidebar, postDetail, relatedPosts);
        HBox.setHgrow(mainContentContainer.getChildren().get(1), Priority.ALWAYS);
    }

}
