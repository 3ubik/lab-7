package lab7;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class DialogFrame extends JFrame {

	private static final int FROM_FIELD_DEFAULT_COLUMNS = 10;
	private static final int TO_FIELD_DEFAULT_COLUMNS = 20;
	private static final int INCOMING_AREA_DEFAULT_ROWS = 10;
	private static final int OUTGOING_AREA_DEFAULT_ROWS = 5;
	private static final int SMALL_GAP = 5;
	private static final int MEDIUM_GAP = 10;
	private static final int LARGE_GAP = 15;
	private static final int WIDTH = 250;
	private static final int HEIGHT = 400;
	private InstantMessenger messenger;
	private final JTextArea textAreaIn;
	private final JTextArea textAreaOut;
	
	public JTextArea getTextAreaIn() {
		return textAreaIn;
	}

	public JTextArea getTextAreaOut() {
		return textAreaOut;
	}

	public DialogFrame(final User user, MainFrame frame){
		
		messenger = frame.getMessenger();
		setTitle("Áåñåäà ñ " + user.getName());
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		
		final Toolkit kit = Toolkit.getDefaultToolkit();
		setLocation((kit.getScreenSize().width - getWidth())/2, (kit.getScreenSize().height - getHeight())/2);
		
		textAreaIn = new JTextArea(INCOMING_AREA_DEFAULT_ROWS, 0);
		textAreaIn.setEditable(false);
		final JScrollPane scrollPaneIncoming = new JScrollPane(textAreaIn);
		
		textAreaOut = new JTextArea(OUTGOING_AREA_DEFAULT_ROWS, 0);
		final JScrollPane scrollPaneOutgoing = new JScrollPane(textAreaOut);
		
		final JPanel messagePanel = new JPanel();
		messagePanel.setBorder(BorderFactory.createTitledBorder("Ñîîáùåíèå"));
		final JButton sendButton = new JButton("Îòïðàâèòü");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				messenger.sendMessage(user, textAreaOut.getText(), PrivateDialogFrame.this);
			}
		});
		
		final GroupLayout layout2 = new GroupLayout(messagePanel);
		messagePanel.setLayout(layout2);
		layout2.setHorizontalGroup(layout2.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout2.createParallelGroup(Alignment.TRAILING)
						.addGroup(layout2.createSequentialGroup())
							.addComponent(scrollPaneOutgoing)
							.addComponent(sendButton))
				.addContainerGap());
			layout2.setVerticalGroup(layout2.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout2.createParallelGroup(Alignment.BASELINE))
					.addGap(MEDIUM_GAP)
					.addComponent(scrollPaneOutgoing)
					.addGap(MEDIUM_GAP)
					.addComponent(sendButton)
					.addContainerGap());
			
			final GroupLayout layout1 = new GroupLayout(getContentPane());
			setLayout(layout1);
			layout1.setHorizontalGroup(layout1.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout1.createParallelGroup()
								.addComponent(scrollPaneIncoming)
								.addComponent(messagePanel))
						.addContainerGap());
			layout1.setVerticalGroup(layout1.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPaneIncoming)
					.addGap(MEDIUM_GAP)
					.addComponent(messagePanel)
					.addContainerGap());
		setVisible(true);
	}
	
}