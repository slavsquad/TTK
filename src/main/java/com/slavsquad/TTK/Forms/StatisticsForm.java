// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.slavsquad.TTK.Forms;

import com.slavsquad.TTK.Text.Dictionaries;
import com.slavsquad.TTK.Text.Dictionary;
import com.slavsquad.TTK.Text.Text;
import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.UnitType;
//import sun.security.pkcs11.wrapper.CK_UNLOCKMUTEX;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class <>StatisticsForm</> implements form, displaying statistics information
 */
public class StatisticsForm extends JDialog
{
    Dictionaries dictionaries;
    JPanel xyPanel,cardPanel;
    final JComboBox<String> namesDictionaries;
    final static String ELECTION = "Card when user choosing dictionary from pie cart";
    final static String STATISTIC = "Card when displays statistics for choosing dictionary";

    /**
     * Constructor StatisticsForm creating new form
     * @param parent - needs pointing parent form, because StatisticsForm is modal dialog form
     * @param title - string which contains title form
     * @param dictionaries - field stores list of dictionaries
     */
    public StatisticsForm(JFrame parent, String title, Dictionaries dictionaries)
    {
        super(parent,title);
        this.dictionaries = dictionaries;
        namesDictionaries = new JComboBox<>();
        JLabel dictionariesLabel = new JLabel("Select dictionary: ");
        namesDictionaries.insertItemAt(null,0);
        namesDictionaries.setSelectedIndex(0);

        ChartPanel chartPiePanel = createPiePanel();
        PiePlot piePlot = (PiePlot)chartPiePanel.getChart().getPlot();
        DefaultPieDataset pieDataset = (DefaultPieDataset) piePlot.getDataset();

        for (Object key:pieDataset.getKeys()){
            namesDictionaries.addItem((String)key);
        }

        namesDictionaries.addActionListener(e -> {
            if (namesDictionaries.getSelectedItem()!=null){
                createdStatisticPanel(namesDictionaries.getSelectedItem().toString());
            }
        });

        JPanel piePanel = new JPanel();
        JPanel titleChar = new JPanel(new GridBagLayout());

        titleChar.setBackground(Color.white);
        titleChar.add(dictionariesLabel,new GridBagConstraints(0,0,1,1,0,0, GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0));
        titleChar.add(namesDictionaries, new GridBagConstraints(1,0,1,1,1,0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,5),0,0));

        piePanel.setLayout(new BorderLayout());
        piePanel.add(titleChar,BorderLayout.NORTH);
        piePanel.add(createPiePanel(),BorderLayout.CENTER);

        cardPanel = new JPanel(new CardLayout());
        xyPanel = new JPanel();
        cardPanel.add(piePanel,ELECTION);
        setContentPane(cardPanel);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setSize(640,480);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Method creates new pieDataset which containing data for pieChart
     * @return pieDataset
     */
    private  PieDataset createPieDataset()
    {
        DefaultPieDataset defaultpiedataset = new DefaultPieDataset();
        for (Dictionary dictionary:dictionaries.getListDictionaries()){
            if (dictionary.getIdCurrentText()>0)
                defaultpiedataset.setValue(dictionary.getName(), dictionary.getIdCurrentText());
        }
        return defaultpiedataset;
    }

    /**
     * Method creates new pieChart which displaying all dictionaries and percentage of complition
     * @param piedataset - dataset for pie chart
     * @return jfreechart precisely pie chart
     */
    private  JFreeChart createPieChart(PieDataset piedataset)
    {
        JFreeChart jfreechart = ChartFactory.createPieChart(null, piedataset, false, true, false);
        PiePlot pieplot = (PiePlot) jfreechart.getPlot();


        pieplot.setNoDataMessage("No statistic data available!");

        //explode all sections
        for (Object key:piedataset.getKeys()){
            pieplot.setExplodePercent((Comparable) key,0.03);
        }


        pieplot.setAutoPopulateSectionPaint(false);
        pieplot.setBaseSectionPaint(new Color(66, 76, 196));
        pieplot.setShadowPaint(null);


        pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator("{1}"));
        pieplot.setLabelBackgroundPaint(null);
        pieplot.setSimpleLabelOffset(new RectangleInsets(UnitType.RELATIVE, 0.05, 0.05, 0.05, 0.05));
        pieplot.setLabelFont(new Font("Veranda", Font.BOLD, 12));
        pieplot.setLabelPaint(Color.WHITE);
        pieplot.setSimpleLabels(true);
        pieplot.setLabelShadowPaint(null);
        pieplot.setLabelOutlinePaint(null);
        pieplot.setToolTipGenerator(new StandardPieToolTipGenerator("<html>{0}<br><b>{1} ({2})</b></html>"));
        pieplot.setInteriorGap(0.02D);

        return jfreechart;
    }

    /**
     * Method creates panel in which contains pieChart
     * @return chart panel
     */
    public ChartPanel createPiePanel()
    {
        JFreeChart jfreechart = createPieChart(createPieDataset());
        PiePlot piePlot = (PiePlot)jfreechart.getPlot();

//
        ChartPanel chartpanel = new ChartPanel(jfreechart);
        chartpanel.setMouseWheelEnabled(true);
        chartpanel.addChartMouseListener(new PieMouseListener(piePlot));
        return chartpanel;
    }

    /**
     * Class <>PieMouseListener</> implements listener for mouse click on a pie chart
     */
    class PieMouseListener implements ChartMouseListener{

        private PiePlot piePlot;

        /**
         * Construct new instance PieMouseListener
         * @param piePlot - plot from pie chart
         */
        public PieMouseListener(PiePlot piePlot){
            this.piePlot = piePlot;
        }

        /**
         * Method receives notification of a mouse click on a chart.
         */
        @Override
        public void chartMouseClicked(ChartMouseEvent chartMouseEvent) {
            ChartEntity entity = chartMouseEvent.getEntity();

            if(entity instanceof PieSectionEntity){
                PieSectionEntity pieEntity = (PieSectionEntity)entity;
                String dictionaryName = pieEntity.getSectionKey().toString();
                createdStatisticPanel(dictionaryName);
                clearSection(piePlot);
            }
        }

        /**
         * Method receives notification of a mouse move on a chart.
         */
        @Override
        public void chartMouseMoved(ChartMouseEvent chartMouseEvent) {

            ChartEntity entity = chartMouseEvent.getEntity();
            if(entity instanceof PieSectionEntity){
                getRootPane().setCursor(new Cursor(Cursor.HAND_CURSOR));
                PieSectionEntity pieSectionEntity = (PieSectionEntity)entity;

                clearSection(piePlot);

                piePlot.setSectionOutlinePaint(pieSectionEntity.getSectionKey(),Color.WHITE);
                piePlot.setSectionOutlineStroke(pieSectionEntity.getSectionKey(),new BasicStroke(1.5f));
                piePlot.setSectionPaint(pieSectionEntity.getSectionKey(),new Color(115, 136, 196));
            }else {
                clearSection(piePlot);
                getRootPane().setCursor(Cursor.getDefaultCursor());
            }
        }

        /**
         * Method clears section's paints, outline's stroke and paints
         */
        private void clearSection(PiePlot piePlot){
            piePlot.clearSectionOutlineStrokes(true);
            piePlot.clearSectionOutlinePaints(true);
            piePlot.clearSectionPaints(true);
        }
    }

    /**
     * Method creates and shows statistic panel
     */
    private void createdStatisticPanel(String dictionaryName){
        Dictionary dictionary = dictionaries.getDictionary(dictionaryName);

        CardLayout cl = (CardLayout)(cardPanel.getLayout());
        xyPanel = createXyPanel(dictionaryName,0,dictionary.getIdCurrentText()-1,"Speed");

        cardPanel.add(xyPanel,STATISTIC);

        cl.show(cardPanel,STATISTIC);
    }

    //statistic panel
    /**
     * Class <>XYMouseListener</> implements listener for mouse click on a XY chart
     */
    class XYMouseListener implements ChartMouseListener{
        private String name;
        private MyBarRenderer barRenderer;
        private MySplineRenderer splineRenderer;
        private String category;

        /**
         * Construct new XYMouseListener
         * @param name - dictionary's name
         * @param splineRenderer - spline renderer for xychart
         * @param barRenderer - bar renderer for bar chart
         * @param category - name of category, which choosing user(Speed, Errors, Volume)
         */
        public XYMouseListener(String name,MySplineRenderer splineRenderer, MyBarRenderer barRenderer,String category){
            this.name = name;
            this.barRenderer = barRenderer;
            this.splineRenderer = splineRenderer;
            this.category = category;
        }
        /**
         * Method receives notification of a mouse click on a chart.
         */
        @Override
        public void chartMouseClicked(ChartMouseEvent chartMouseEvent) {
            ChartEntity entity = chartMouseEvent.getEntity();
            String toolTip = entity.getToolTipText();

            if(entity instanceof XYItemEntity && toolTip!=null){
                Dictionary dictionary = dictionaries.getDictionary(name);
                XYItemEntity xyItemEntity = (XYItemEntity)entity;
                TimeSeriesCollection timeSeries = (TimeSeriesCollection) chartMouseEvent.getChart().getXYPlot().getDataset(0);
                int startTextId = timeSeries.getY(4,xyItemEntity.getItem()).intValue();
                int endTextId = timeSeries.getY(5,xyItemEntity.getItem()).intValue();

                if (timeSeries.getSeries(0).getItemCount()==1){
                    startTextId = 0;
                    endTextId = dictionary.getIdCurrentText()-1;
                    //return; if requires stop the user's clicking on the last point
                }

                refreshChart(name,startTextId,endTextId,category);
            }
        }

        /**
         * Method receives notification of a mouse move on a chart.
         */
        @Override
        public void chartMouseMoved(ChartMouseEvent chartMouseEvent) {
            ChartEntity chartentity = chartMouseEvent.getEntity();

            if(!(chartentity instanceof XYItemEntity))
            {
                clearHighligting();
            } else{
                XYItemEntity xyItemEntity = (XYItemEntity)chartentity;
                TimeSeriesCollection timeSeriesCollection = (TimeSeriesCollection)xyItemEntity.getDataset();
                TimeSeries timeSeries = timeSeriesCollection.getSeries(xyItemEntity.getSeriesIndex());

                if (timeSeries.getKey().toString().equals("Volume of texts")){
                    clearHighligting();
                } else{
                    getRootPane().setCursor(new Cursor(Cursor.HAND_CURSOR));
                    barRenderer.setHighlightedItem(0, xyItemEntity.getItem());
                    if (!timeSeries.getKey().toString().equals("Count of texts")){
                        splineRenderer.setHighlightedItem(xyItemEntity.getSeriesIndex(),xyItemEntity.getItem());
                    }else {
                        splineRenderer.setHighlightedItem(xyItemEntity.getItem());
                    }
                }
            }
        }

        /**
         * Method clear all highligting and set cursor by default
         */
        private void clearHighligting(){
            getRootPane().setCursor(Cursor.getDefaultCursor());
            barRenderer.setHighlightedItem(-1, -1);
            splineRenderer.setHighlightedItem(-1,-1);
            splineRenderer.setHighlightedItem(-1);
        }
    }


    /**
     * Method creates new XY data set which containing data for XY Chart
     * @param name - name of dictionary
     * @param startTextId - text id which to start displaying statistics chart
     * @param endTextId - text id which to end displaying statistics chart
     * @param category - statistic's category which selected user
     * @return data set for xy chart
     */
    private  TimeSeriesCollection createXyDataset(String name,int startTextId, int endTextId, String category)
    {
        /**
         * Class <>MySecond</> implements customised time second's interval
         */
        class MySecond extends Second{
            long range;

            /**
             * Construct new time second's interval
             * @param date - start of interval
             * @param range - end of interval
             */
            public MySecond(Date date,long range){
                super(date);
                this.range = range;
            }

            /**
             * Method gets milliseconds from start interval
             * @param calendar - type of using calendar
             * @return first millisecond for time interval
             */
            @Override
            public long getFirstMillisecond(Calendar calendar) {
                return super.getFirstMillisecond(calendar);
            }

            /**
             * Method gets milliseconds from end interval
             * @param calendar - type of using calendar
             * @return last millisecond for time interval
             */
            @Override
            public long getLastMillisecond(Calendar calendar) {
                return super.getLastMillisecond(calendar)+range;
            }
        }

        TimeSeries speedSeries = new TimeSeries("Average "+category);
        TimeSeries maxSpeedSeries = new TimeSeries("Maximum "+category);
        TimeSeries minSpeedSeries = new TimeSeries("Minimum "+category);
        TimeSeries countBarSeries = new TimeSeries("Count of texts");
        TimeSeries startIntervalSeries = new TimeSeries("startInterval");
        TimeSeries endIntervalSeries = new TimeSeries("endInterval");
        TimeSeries volumeTextSeries= new TimeSeries("Volume of texts");
        TimeSeries volumePointsSeries= new TimeSeries("Volume points");


        Dictionary dictionary = dictionaries.getDictionary(name);



        int inter = 12;
        long range;

        long startInterval,endInterval,startRangeInterval;
        int interval = 0;

        long start = dictionary.getText(startTextId).getDate().getTime();
        long end = dictionary.getText(endTextId).getDate().getTime();

        if (start!=end){
            range = (end-start)/inter;
            startInterval = dictionary.getText(startTextId).getDate().getTime();
        }else {
            range = dictionary.getText(endTextId).getTime();
            startInterval = dictionary.getText(endTextId).getDate().getTime()-dictionary.getText(endTextId).getTime();
        }
        startRangeInterval =  startInterval + range;
        endInterval=startRangeInterval;


        double max = 0;
        double min = dictionary.getMaxSpeed();
        double sum = 0;
        double count = 0;


        for (int i=startTextId;i<=endTextId;i++) {
            Text text = dictionary.getText(i);
            long date = text.getDate().getTime();
            double indicator=0;
            if (category.equals("Speed")){
                indicator = text.getSpeed();
            }
            if (category.equals("Errors")){
                indicator = text.getError();
            }

            //Calculates data set for statistics form
            while (!(date >= startInterval && date <= endInterval)) {
                if (count > 0) {
                    speedSeries.add(new Second(new Date(startInterval + range / 2)), sum / count);
                    maxSpeedSeries.add(new Second(new Date(startInterval + range / 2)), max);
                    minSpeedSeries.add(new Second(new Date(startInterval + range / 2)), min);
                    MySecond second = new MySecond(new Date(startInterval),range);
                    countBarSeries.add(second, count);
                    endIntervalSeries.add(new Second(new Date(endInterval)),i-1);
                    double volume = i-startTextId;
                    volumeTextSeries.add(new Second(new Date(endInterval)),volume);
                    volumePointsSeries.add(new Second(new Date(endInterval)),volume);
                }

                interval++;
                startInterval = endInterval;
                endInterval = startRangeInterval + range * interval;

                if (interval == (inter-1)) {
                    endInterval = dictionary.getText(endTextId).getDate().getTime();
                }


                max = 0;
                min = dictionary.getMaxSpeed();
                sum = 0;
                count = 0;

            }

            max = Math.max(max, indicator);
            min = Math.min(min, indicator);
            sum += indicator;
            count++;
            if(count==1){
                startIntervalSeries.add(new Second(new Date(startInterval)),i);
            }

        }

        if(count!=0){
            speedSeries.add(new Second(new Date(startInterval+range/2)),sum/count);
            maxSpeedSeries.add(new Second(new Date(startInterval+range/2)),max);
            minSpeedSeries.add(new Second(new Date(startInterval+range/2)),min);
            MySecond second = new MySecond(new Date(startInterval),range);
            countBarSeries.add(second, count);
            endIntervalSeries.add(new Second(new Date(endInterval)),endTextId);
            volumeTextSeries.add(new Second(new Date(endInterval)),endTextId+1-startTextId);
            volumePointsSeries.add(new Second(new Date(endInterval)),endTextId+1-startTextId);
        }

        TimeSeriesCollection seriescollection = new TimeSeriesCollection();
        seriescollection.addSeries(maxSpeedSeries);
        seriescollection.addSeries(minSpeedSeries);
        seriescollection.addSeries(speedSeries);
        seriescollection.addSeries(countBarSeries);
        seriescollection.addSeries(startIntervalSeries);
        seriescollection.addSeries(endIntervalSeries);
        seriescollection.addSeries(volumeTextSeries);
        seriescollection.addSeries(volumePointsSeries);

        return seriescollection;
    }


    /**
     * Class <>MyBarRenderer</> implements renderer for bar chart
     */
    class MyBarRenderer extends XYBarRenderer
    {

        /**
         * Construct new bar renderer
         */
        public MyBarRenderer(){
            this(0.0D);
        }

        /**
         * Construct new bar renderer
         * @param margin - width bar, from 0.0 = 100% to 1.0 = 0% width of one bar
         */
        public MyBarRenderer(double margin)
        {
            super(margin);
            highlightRow = -1;
            highlightColumn = -1;
        }

        /**
         * Method sets item witch needed highlighted
         * @param i - series id
         * @param j - item id
         */
        public void setHighlightedItem(int i, int j)
        {
            if(highlightRow != i && highlightColumn != j)
            {
                highlightRow = i;
                highlightColumn = j;
                notifyListeners(new RendererChangeEvent(this));
            }
        }

        /**
         * Method return outline color for selected item
         * @param i - series id
         * @param j - item id
         * @return paint outline color
         */
        @Override
        public Paint getItemOutlinePaint(int i, int j)
        {
            if(i == highlightRow && j == highlightColumn)
                return Color.white;
            else
                return super.getItemOutlinePaint(i, j);
        }

        private int highlightRow;
        private int highlightColumn;

    }

    /**
     * Class <>MySplineRenderer</> implements renderer for xy chart
     */
    class MySplineRenderer extends XYSplineRenderer
    {
        private int highlightRow;
        private int highlightColumn;
        private boolean paintAllRow;

        /**
         * Construct new renderer
         */
        public MySplineRenderer()
        {
            highlightRow = -1;
            highlightColumn = -1;
        }

        /**
         * Method sets item witch needed highlighted
         * @param i - series id
         * @param j - item id
         */
        public void setHighlightedItem(int i, int j)
        {
            paintAllRow = false;

            if(highlightRow != i && highlightColumn != j)
            {
                highlightRow = i;
                highlightColumn = j;
                notifyListeners(new RendererChangeEvent(this));
            }
        }


        /**
         * Method sets outline color selected item for all series
         * @param j - item id
         */
        public void setHighlightedItem(int j)
        {
            paintAllRow = true;

            if(highlightColumn != j)
            {
                highlightColumn = j;
                notifyListeners(new RendererChangeEvent(this));
            }
        }

        /**
         * Method return outline color for selected item
         * @param i - series id
         * @param j - item id
         * @return paint outline color
         */
        @Override
        public Paint getItemOutlinePaint(int i, int j)
        {
            if (paintAllRow){
                if(j == highlightColumn)
                    return Color.white;
                else
                    return super.getItemOutlinePaint(i, j);
            }else {
                if(i == highlightRow && j == highlightColumn)
                    return Color.white;
                else
                    return super.getItemOutlinePaint(i, j);
            }
        }
    }

    /**
     * Method creates xy chart
     * @param dataset - data set for xy chart
     * @param category - statistic's category
     * @return jfreecart precisely xy chart
     */
    private JFreeChart createXyChart(TimeSeriesCollection dataset, final String category)
    {
        ToolTipManager ttm =
                ToolTipManager.sharedInstance();
        ttm.setLightWeightPopupEnabled(false);
        ttm.setInitialDelay(100);
        ttm.setDismissDelay(30000);
        ttm.setReshowDelay(1000);

        /**
         * Class <>XYToolTipsGenerator</> implements generation tool tips for xy chart
         */
        class XYToolTipsGenerator implements XYToolTipGenerator{
            private XYDataset dataset;
            public XYToolTipsGenerator(XYDataset dataset){
                this.dataset = dataset;
            }

            /**
             * Method generate tool tips for chart
             * @param dataset - stores data sets for chart
             * @param series - id selected series
             * @param item - id selected item
             * @return string with tool tip
             */
            public String generateToolTip(XYDataset dataset, int series, int item) {
                dataset = this.dataset;
                StringBuilder stringBuilder = new StringBuilder("");
                Number average = dataset.getY(2, item);
                Number max = dataset.getY(0, item);
                Number min = dataset.getY(1, item);
                Number countText = dataset.getY(3,item);
                Number volume = dataset.getY(6,item);
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
                long startInterval = dataset.getX(4,item).longValue();
                long endInterval = dataset.getX(5,item).longValue();

                String period = dateFormat.format(new Date(startInterval)) +" - "+ dateFormat.format(new Date(endInterval));

                stringBuilder.append(String.format("<html><p style='color:#0000ff;'><b><!#!>%s<!#!></p>", period));
                if (category.equals("Speed")){
                    stringBuilder.append(String.format("Aver %s:<b> %d ch/min</b><br/>",category, average.intValue()));
                    stringBuilder.append(String.format("Max %s:<b> %d ch/min</b><br/>",category, max.intValue()));
                    stringBuilder.append(String.format("Min %s:<b> %d ch/min</b><br/>",category, min.intValue()));
                    stringBuilder.append(String.format("Count of texts:<b> %d </b><br/>", countText.intValue()));
                }
                if (category.equals("Errors")){
                    stringBuilder.append(String.format("Aver  Errors:<b> %1$.2f%%</b><br/>",average.doubleValue()));
                    stringBuilder.append(String.format("Max  Errors:<b> %1$.2f%%</b><br/>",max.doubleValue()));
                    stringBuilder.append(String.format("Min  Errors:<b> %1$.2f%%</b><br/>",min.doubleValue()));
                    stringBuilder.append(String.format("Count of texts:<b> %d </b><br/>",countText.intValue()));
                }
                if (category.equals("Volume")){
                    stringBuilder.append(String.format("%s of tests:<b>%d</b><br/>",category, volume.intValue()));
                    stringBuilder.append(String.format("Count of texts:<b> %d </b><br/>",countText.intValue()));
                }

                stringBuilder.append("</html>");

                return stringBuilder.toString();
            }
        }


        MySplineRenderer xySplineRenderer = new MySplineRenderer();
        xySplineRenderer.setBaseToolTipGenerator(new XYToolTipsGenerator(dataset));

        xySplineRenderer.setSeriesVisible(3,false);
        xySplineRenderer.setSeriesVisible(4,false);
        xySplineRenderer.setSeriesVisible(5,false);
        xySplineRenderer.setSeriesVisible(6,false);



        DateAxis dateaxis = new DateAxis("");
        dateaxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);

        NumberAxis splineNumberAxis = new NumberAxis(category);

        XYPlot xyplot = new XYPlot(dataset, dateaxis, splineNumberAxis, xySplineRenderer);

        IntervalXYDataset barDataset = new TimeSeriesCollection(dataset.getSeries(3));

        MyBarRenderer xyBarRenderer = new MyBarRenderer(0.1);
        xyBarRenderer.setBaseToolTipGenerator(new XYToolTipsGenerator(dataset));

        xyBarRenderer.setDrawBarOutline(true);
        xyBarRenderer.setBaseOutlineStroke(new BasicStroke(1.5f));//set width outline
        xyBarRenderer.setSeriesOutlinePaint(0,Color.BLACK);

        NumberAxis barNumberAxis = new NumberAxis("Count of Texts");
        barNumberAxis.setUpperMargin(2.0);//Add empty space from max y to up chart, 2.0 = 200%
        barNumberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());//Integer ticks instead double



        XYAreaRenderer xyAreaRenderer = new XYAreaRenderer();
        TimeSeriesCollection timeSeriesCollectionArea = new TimeSeriesCollection(dataset.getSeries(6));

        if (category.equals("Volume")){
            xySplineRenderer.setSeriesVisible(0,false);
            xySplineRenderer.setSeriesVisible(1,false);
            xySplineRenderer.setSeriesVisible(2,false);
            xySplineRenderer.setSeriesLinesVisible(7,false);
            xyBarRenderer.setSeriesVisible(0,false);
        }else {
            xySplineRenderer.setSeriesVisible(7,false);
            xyAreaRenderer.setSeriesVisible(0,false);
            xyplot.setRangeAxis(1, barNumberAxis);
            xyplot.mapDatasetToRangeAxis(1,1);
        }

        xyplot.setDataset(1, barDataset);
        xyplot.setRenderer(1, xyBarRenderer);
        xyplot.setDataset(2,timeSeriesCollectionArea);
        xyplot.setRenderer(2,xyAreaRenderer);


        JFreeChart jfreechart;
        jfreechart = new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
        ChartUtilities.applyCurrentTheme(jfreechart);


        xyBarRenderer.setBarPainter(new StandardXYBarPainter());
        xyBarRenderer.setSeriesPaint(0,new Color(0xFF8367));

        Shape shape =  new Ellipse2D.Double(-2.5, -2.5, 5.0, 5.0);

        xySplineRenderer.setSeriesShape(0,shape);
        xySplineRenderer.setSeriesShape(1,shape);
        xySplineRenderer.setSeriesShape(2,shape);
        xySplineRenderer.setSeriesShape(4,shape);
        xySplineRenderer.setSeriesShape(7,shape);

        xySplineRenderer.setSeriesVisibleInLegend(7,false);

        xyAreaRenderer.setSeriesPaint(0,new Color(102, 110, 229,100));


        xySplineRenderer.setSeriesPaint(0,new Color(229, 62, 90));
        xySplineRenderer.setSeriesPaint(1,new Color(136, 59, 179));
        xySplineRenderer.setSeriesPaint(2,new Color(102, 110, 229));
        xySplineRenderer.setSeriesPaint(7,new Color(102, 110, 229));

        xySplineRenderer.setUseOutlinePaint(true);
        xySplineRenderer.setSeriesOutlinePaint(0,new Color(229, 62, 90));
        xySplineRenderer.setSeriesOutlinePaint(1,new Color(136, 59, 179));
        xySplineRenderer.setSeriesOutlinePaint(2,new Color(102, 110, 229));
        xySplineRenderer.setSeriesOutlinePaint(7,new Color(102, 110, 229));

        xySplineRenderer.setBaseOutlineStroke(new BasicStroke(2.5f));//set width outline

        return jfreechart;
    }


    /**
     * Method creates panel for xy chart
     * @param name - name of dictionary
     * @param startTextId - text id which to start displaying statistics chart
     * @param endTextId - text id which to end displaying statistics chart
     * @param category - statistic's category which selected user
     * @return panel for xy chart
     */
    public JPanel createXyPanel(final String name, int startTextId,int endTextId,String category)
    {
        getRootPane().setCursor(Cursor.getDefaultCursor());
        final JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Speed");
        comboBox.addItem("Errors");
        comboBox.addItem("Volume");
        comboBox.setSelectedItem(category);

        JFreeChart jfreechart = createXyChart(createXyDataset(name,startTextId,endTextId,category),category);

        class SelectCategoryListener implements ActionListener{
            private int startTextId;
            private int endTextId;
            public SelectCategoryListener(int startTextId,int endTextId){
                this.startTextId = startTextId;
                this.endTextId = endTextId;
            }

            @Override
            public void actionPerformed(ActionEvent e){
                refreshChart(name,startTextId,endTextId,comboBox.getSelectedItem().toString());
            }
        }

        comboBox.addActionListener(new SelectCategoryListener(startTextId,endTextId));


        JPanel chartPanel = new JPanel();
        JPanel titleChar = new JPanel();

        titleChar.setBackground(Color.white);
        titleChar.setLayout(new FlowLayout(FlowLayout.RIGHT));
        titleChar.add(new JLabel("Indicators: "));

        titleChar.add(comboBox);
        chartPanel.setLayout(new BorderLayout());
        chartPanel.add(titleChar,BorderLayout.NORTH);

        XYPlot plot = (XYPlot) jfreechart.getPlot();

        MySplineRenderer splineRenderer = (MySplineRenderer)plot.getRenderer(0);
        MyBarRenderer mybarrenderer = (MyBarRenderer)plot.getRenderer(1);

        ChartPanel cp = new ChartPanel(jfreechart);
        cp.addChartMouseListener(new XYMouseListener(name,splineRenderer,mybarrenderer,category));

        chartPanel.add(cp,BorderLayout.CENTER);
        JButton button = new JButton("Back");

        button.setToolTipText("Return to choosing dictionary");

        button.addActionListener(e -> {
            CardLayout cl = (CardLayout)(cardPanel.getLayout());
            cl.show(cardPanel,ELECTION);
            namesDictionaries.setSelectedIndex(0);
        });

        JPanel bottomChart =  new JPanel();
        bottomChart.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomChart.setBackground(Color.white);
        bottomChart.add(button);
        chartPanel.add(bottomChart,BorderLayout.SOUTH);
        return chartPanel;

    }

    /**
     * Method update chart when necessary
     * @param name - name of dictionary
     * @param startTextId - text id which to start displaying statistics chart
     * @param endTextId - text id which to end displaying statistics chart
     * @param category - statistic's category which selected user
     */
    private void refreshChart(String name, int startTextId, int endTextId, String category) {
        CardLayout cl = (CardLayout)(cardPanel.getLayout());
        xyPanel = createXyPanel(name,startTextId,endTextId,category);

        cardPanel.add(xyPanel,STATISTIC);

        cl.show(cardPanel,STATISTIC);
    }
}


