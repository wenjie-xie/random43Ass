/**
 * 
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gui.pages.DataInsertBookPanel;
import gui.pages.DataInsertMoviePanel;
import gui.pages.DataInsertMusicPanel;
import gui.pages.DataRemoveSearchPanel;
import gui.pages.DataUpdateSearchPanel;
import gui.pages.HomePagePanel;
import gui.pages.ViewFilterPanel;
import gui.pages.reports.ReportAuthorsPublication;
import gui.pages.reports.ReportPublicationInOneYear;

/**
 * @author xiewen4
 * This is the home page of the Home Library
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 7595726794122456110L;
	
	private JScrollPane currPanel;
	
	public MainFrame(String title) {
		super(title);
		
		// Set the default page as home page
		HomePagePanel homePagePanel = new HomePagePanel();
		homePagePanel.setBounds(0, 0, 1500, 800);
		JScrollPane panelScroll = new JScrollPane(homePagePanel);
		this.currPanel = panelScroll;
		this.getContentPane().add(panelScroll);
		
		
		// Create the navigation menu
		JMenuBar menuBar = new JMenuBar();
		
		// Data Menu
		JMenu dataMenu = instantiateDataMenu();
		menuBar.add(dataMenu);
		
		
		// View Menu
		JMenuItem viewMenu = createViewMenu();
		menuBar.add(viewMenu);
		
		// Report Menu
		JMenu reportMenu = createReportMenu();
		menuBar.add(reportMenu);
		
		// Add menu bar
		this.setJMenuBar(menuBar);
	}
	
	
	/**
	 * The purpose of this function is to initialize Data Menu
	 * @return the Data menu
	 */
	private JMenu instantiateDataMenu() {
		// Data Menu
		JMenu dataMenuBar = new JMenu("Data");
		
		// Data > Insert
		JMenu dataInsertMenu = this.createDataInsertMenu();
		dataMenuBar.add(dataInsertMenu);
		
		// Data > Update
		JMenuItem dataUpdateMenu = this.createDataUpdateMenu();
		dataMenuBar.add(dataUpdateMenu);
		
		// Data > Remove
		JMenuItem dataRemoveMenu = this.createDataRemoveMenu();
		dataMenuBar.add(dataRemoveMenu);
		
		return dataMenuBar;
	}
	
	
	/**
	 * Create Insert Menu
	 * @return insert menu
	 */
	private JMenu createDataInsertMenu() {
		// Data > Insert
		JMenu dataInsert = new JMenu("Insert");
		// Data > Insert > Book
		JMenuItem book = new JMenuItem("Book");
		book.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				flipPageTo(new DataInsertBookPanel());
			}
		});
		dataInsert.add(book);
		
		// Data > Insert > Music
		JMenuItem music = new JMenuItem("Music");
		music.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				flipPageTo(new DataInsertMusicPanel());
			}
		});
		dataInsert.add(music);
		
		// Data > Insert > Movie
		JMenuItem movie = new JMenuItem("Movie");
		movie.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				flipPageTo(new DataInsertMoviePanel());
			}
		});
		dataInsert.add(movie);
		
		return dataInsert;
	}
	
	
	/**
	 * Create Update Menu
	 * @return update menu
	 */
	private JMenuItem createDataUpdateMenu() {
		// Data > Update
		JMenuItem dataUpdate = new JMenuItem("Update");
		dataUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				flipPageTo(new DataUpdateSearchPanel());
			}
		});
		
		return dataUpdate;
	}
	
	/**
	 * Create Remove Menu
	 * @return update menu
	 */
	private JMenuItem createDataRemoveMenu() {
		// Data > Update
		JMenuItem dataUpdate = new JMenuItem("Remove");
		dataUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				flipPageTo(new DataRemoveSearchPanel());
			}
		});
		
		return dataUpdate;
	}
	
	
	/**
	 * Create View Menu
	 * @return update menu
	 */
	private JMenuItem createViewMenu() {
		// Data > Update
		JMenuItem view = new JMenuItem("View");
		view.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				flipPageTo(new ViewFilterPanel());
			}
		});
		
		return view;
	}
	
	
	/**
	 * Create the report menu
	 * @return the report menu
	 */
	private JMenu createReportMenu() {
		JMenu report = new JMenu("Report");
		
		JMenuItem r1 = new JMenuItem("Authors’ Publications");
		r1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				flipPageTo(new ReportAuthorsPublication());
				
			}
		});
		report.add(r1);
		
		JMenuItem r2 = new JMenuItem("Publications in one Year");
		r2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				flipPageTo(new ReportPublicationInOneYear());
				
			}
		});
		report.add(r2);
		
		JMenuItem r3 = new JMenuItem("Books Similar Topic");
		r3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		report.add(r3);
		
		JMenuItem r4 = new JMenuItem("Frequent Publishers");
		r4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		report.add(r4);
		
		JMenuItem r5 = new JMenuItem("Most Popular Subjects");
		r5.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		report.add(r5);
		
		JMenuItem r6 = new JMenuItem("Multi Skill Movie Crew");
		r6.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		report.add(r6);
		
		JMenuItem r7 = new JMenuItem("Award Winning Movies");
		r7.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		report.add(r7);
		
		JMenuItem r8 = new JMenuItem("Music With Similar Name");
		r8.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		report.add(r8);
		
		JMenuItem r9 = new JMenuItem("Multi Skills Music Crew");
		r9.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		report.add(r9);
		
		JMenuItem r10 = new JMenuItem("Similar Names");
		r10.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		report.add(r10);
		
		return report;
	}
	
	
	/**
	 * The purpose of this method is to change the current Panel to the
	 * Panel given. Resulting in a page change.
	 * @param nextPanel
	 */
	public void flipPageTo(JPanel nextPanel) {
		// Change panel
		this.getContentPane().remove(currPanel);
		
		JScrollPane panelScroll = new JScrollPane(nextPanel);
		
		this.getContentPane().add(panelScroll);
		this.currPanel = panelScroll;
		
		// Refresh
		this.revalidate();
		this.repaint();
	}
}
