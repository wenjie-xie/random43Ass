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
		JMenu viewMenu = new JMenu("View");
		
		
		// Report Menu
		JMenu reportMenu = new JMenu("Report");
		
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
