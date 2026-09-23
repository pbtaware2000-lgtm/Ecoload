package com.super_x.view.UserView;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/** Fixed demo Eco Impact dashboard. */
public class EcoImpact {
    private static final String GREEN="#087F43", DARK="#18352B", MUTED="#71807A", BG="#F3FAF6", BORDER="#DFE9E4", PALE="#EAF7F0";
    private static final Monthly[] DATA={new Monthly("April",0,"—","—",0),new Monthly("May",0,"—","—",0),new Monthly("June",0,"—","—",0),new Monthly("July",0,"—","—",0),new Monthly("August",2,"436 km","96 kg CO₂e",96),new Monthly("September",0,"—","—",0)};

    public Scene getEcoImpactScene(){
        VBox content=new VBox(18);content.setPadding(new Insets(24,26,30,26));content.setFillWidth(true);content.getChildren().addAll(heading(),summary(),environment(),monthly());
        ScrollPane scroll=new ScrollPane(content);scroll.setFitToWidth(true);scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;-fx-border-color:transparent;");
        BorderPane page=new BorderPane(scroll);page.setStyle("-fx-background-color:"+BG+";");BorderPane main=new BorderPane(page);main.setTop(UserNavigation.createNavbar());BorderPane root=new BorderPane(main);root.setLeft(UserNavigation.createSidebar("Eco Impact"));return new Scene(root,1536,750,Color.web(BG));
    }
    private VBox heading(){return new VBox(5,label("🌱  ECO IMPACT",DARK,28,true),label("A clear view of your estimated environmental impact across completed loads.",MUTED,14,false));}
    private VBox summary(){VBox box=new VBox(11,label("YOUR DELIVERY SUMMARY",DARK,14,true));GridPane grid=new GridPane();grid.setHgap(14);for(int i=0;i<4;i++){ColumnConstraints c=new ColumnConstraints();c.setPercentWidth(25);c.setHgrow(Priority.ALWAYS);grid.getColumnConstraints().add(c);}grid.add(metric("TOTAL LOADS","2","📦"),0,0);grid.add(metric("COMPLETED / DELIVERED","2","✓"),1,0);grid.add(metric("TOTAL DISTANCE","436 km","↝"),2,0);grid.add(metric("ECO SCORE","82 / 100","🌱"),3,0);box.getChildren().add(grid);return box;}
    private VBox environment(){VBox card=card();HBox row=new HBox(18);row.setAlignment(Pos.CENTER_LEFT);Label icon=label("🌍",GREEN,30,false);icon.setMinWidth(48);VBox copy=new VBox(4,label("ENVIRONMENTAL IMPACT",DARK,14,true),label("🤖  AI Estimated",GREEN,15,true),label("Estimated, not directly measured",MUTED,13,false));Region spacer=new Region();HBox.setHgrow(spacer,Priority.ALWAYS);VBox value=new VBox(2,label("96 kg CO₂e",DARK,28,true),label("Estimated CO₂",MUTED,13,false));value.setAlignment(Pos.CENTER_RIGHT);row.getChildren().addAll(icon,copy,spacer,value);card.getChildren().add(row);return card;}
    private VBox monthly(){return new VBox(12,label("📊  MONTHLY ECO IMPACT",DARK,18,true),label("Track the estimated CO₂ impact for each month in this demo period.",MUTED,14,false),chart(),table());}
    private VBox chart(){
        VBox card=card();HBox header=new HBox();VBox titles=new VBox(3,label("MONTHLY ESTIMATED CO₂ IMPACT",DARK,16,true),label("August contains the two completed demo loads (436 km).",MUTED,13,false));Region spacer=new Region();HBox.setHgrow(spacer,Priority.ALWAYS);Label legend=label("●  Estimated CO₂ (kg CO₂e)","#C66B18",13,true);legend.setStyle(legend.getStyle()+" -fx-background-color:#FFF4E8;-fx-background-radius:12;-fx-padding:6 10;");header.getChildren().addAll(titles,spacer,legend);
        CategoryAxis x=new CategoryAxis();x.setCategories(FXCollections.observableArrayList("April","May","June","July","August","September"));x.setAutoRanging(false);x.setSide(Side.BOTTOM);x.setLabel("Month");x.setTickLabelsVisible(true);x.setTickLabelGap(8);x.setTickLabelFont(Font.font("System",FontWeight.SEMI_BOLD,12));x.setTickLabelFill(Color.web(DARK));x.setStyle("-fx-tick-label-fill:"+DARK+";-fx-font-size:12px;-fx-font-weight:bold;");NumberAxis y=new NumberAxis(0,120,20);y.setLabel("Estimated CO₂ (kg CO₂e)");y.setTickLabelFill(Color.web(MUTED));
        BarChart<String,Number> chart=new BarChart<>(x,y);chart.setLegendVisible(false);chart.setAnimated(false);chart.setCategoryGap(22);chart.setBarGap(4);chart.setHorizontalGridLinesVisible(true);chart.setPrefHeight(340);chart.setMinHeight(320);chart.setMaxWidth(Double.MAX_VALUE);chart.setStyle("-fx-background-color:transparent;-fx-border-color:transparent;");XYChart.Series<String,Number> series=new XYChart.Series<>();series.setName("Estimated CO₂ (kg CO₂e)");XYChart.Data<String,Number> august=new XYChart.Data<>("August",96);august.nodeProperty().addListener((o,old,node)->{if(node!=null)node.setStyle("-fx-bar-fill:#D97920;-fx-background-radius:5 5 0 0;");});series.getData().add(august);chart.getData().add(series);card.getChildren().addAll(header,chart);return card;
    }
    private VBox table(){VBox card=card();card.setSpacing(12);card.getChildren().add(label("MONTHLY ECO IMPACT",DARK,16,true));TableView<Monthly> table=new TableView<>();table.setItems(FXCollections.observableArrayList(DATA));table.setFixedCellSize(44);table.setPrefHeight(310);table.setMinHeight(310);table.setMaxHeight(310);table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);table.setStyle("-fx-background-color:white;-fx-control-inner-background:white;-fx-border-color:"+BORDER+";-fx-border-radius:9;-fx-background-radius:9;-fx-font-size:13px;-fx-table-cell-border-color:"+BORDER+";");TableColumn<Monthly,String> month=column("MONTH",Monthly::month,"-fx-alignment:CENTER-LEFT;");TableColumn<Monthly,Number> loads=new TableColumn<>("LOADS");loads.setCellValueFactory(c->new SimpleIntegerProperty(c.getValue().loads()));loads.setStyle("-fx-alignment:CENTER-RIGHT;-fx-font-weight:bold;");TableColumn<Monthly,String> distance=column("DISTANCE",Monthly::distance,"-fx-alignment:CENTER-RIGHT;");TableColumn<Monthly,String> co2=column("ESTIMATED CO₂",Monthly::co2,"-fx-alignment:CENTER-RIGHT;-fx-font-weight:bold;");table.getColumns().addAll(month,loads,distance,co2);card.getChildren().add(table);return card;}
    private TableColumn<Monthly,String> column(String title,java.util.function.Function<Monthly,String> get,String style){TableColumn<Monthly,String> c=new TableColumn<>(title);c.setCellValueFactory(v->new SimpleStringProperty(get.apply(v.getValue())));c.setStyle(style);return c;}
    private VBox metric(String title,String value,String icon){VBox card=new VBox(10);card.setPadding(new Insets(16));card.setMinHeight(118);card.setPrefHeight(118);card.setStyle("-fx-background-color:white;-fx-background-radius:12;-fx-border-color:"+BORDER+";-fx-border-radius:12;");HBox top=new HBox();top.setAlignment(Pos.CENTER_LEFT);Region spacer=new Region();HBox.setHgrow(spacer,Priority.ALWAYS);Label i=label(icon,GREEN,16,true);i.setStyle(i.getStyle()+" -fx-background-color:"+PALE+";-fx-background-radius:12;-fx-padding:4 7;");top.getChildren().addAll(label(title,DARK,11,true),spacer,i);card.getChildren().addAll(top,label(value,DARK,27,true));return card;}
    private VBox card(){VBox box=new VBox(14);box.setPadding(new Insets(20));box.setStyle("-fx-background-color:white;-fx-background-radius:12;-fx-border-color:"+BORDER+";-fx-border-radius:12;");return box;}
    private Label label(String text,String color,double size,boolean bold){Label l=new Label(text);l.setTextFill(Color.web(color));l.setWrapText(true);l.setMaxWidth(Double.MAX_VALUE);l.setStyle("-fx-text-fill:"+color+";-fx-font-size:"+size+"px;"+(bold?"-fx-font-weight:bold;":""));return l;}
    private record Monthly(String month,int loads,String distance,String co2,int co2Kg){}
}
