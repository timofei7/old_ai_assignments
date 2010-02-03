'''
Created on Feb 1, 2010

@author: tim
'''

from State import State
from Computer import Computer
import sys


s = State()


def HvH():
    print "\nHuman Versus Human!"
    possible = frozenset(['1','2','3','4','5','6', '7'])
    
    while s.legal_moves() != "":
        print s.encode()
        
        # get the move
        input = "get input"
        while (input not in possible):
            input = raw_input("enter move for " + s._next_move() + ":")
            if input == 'q': print "quitter!!"; Quit();
            if input not in possible:
                print "confused... try again? or q to quit..."  
            else:
                #do the move
                move = int(input) 
                if not s.do_move(move): #does the move and checks success
                    input = "keep trying" #if move unsuccessful just loop
                win, player = s.is_win()
                if win:
                    print s.encode()
                    print player + " is the WINNER!!!"
                    sys.exit(0)
                
    
    print s.encode()
    print "No more moves! The Game is a TIE!"
        
        
        
    
def HvC():
    print "Human Versus Computer!"
    
    player_order = ""
    while (player_order != "1" or player_order != "2"):
        player_order = raw_input("go first or second? (enter 1 or 2): ")
    
    
def CvC():
    print "\nunsupported"
def Quit():
    print "\nl8r!"
    sys.exit(0)
def TEST():
    s._test()


if __name__ == '__main__':
        
    if len(sys.argv) > 1:
        file = sys.argv[1]    
        lines = open(file).readlines()
        s.decode("".join(lines))
        
    print """1) Human vs Human
2) Human vs Computer
3) Computer vs Computer
4) Quit
"""
    choice = 0
    while (int(choice) > 5 or int(choice) < 1):
        choice = raw_input("enter your choice: ")
        
    # run option
    {'1': HvH,
     '2': HvC,
     '3': CvC,
     '4': Quit,
     '5': TEST,
    }.get(choice, True)()
    

    
    
    
