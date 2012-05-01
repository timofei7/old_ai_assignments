'''
Created on Feb 1, 2010

@author: tim
for cs44 w10
'''

from State import State
from Computer import Computer
from ComputerPlus import ComputerPlus
import sys


possible = frozenset(['1','2','3','4','5','6', '7'])

s = State()
c = Computer(s)
cp = ComputerPlus(s)
    

def checkWin():
    win, player = s.is_win() #check for win
    if win:
        print s.encode()
        print player + " is the WINNER!!!"
        sys.exit(0)

def getPlayerMove():

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
            checkWin()


def getComputerMove(c, who):
    s.do_move(c.minimaxi(s, who))
    checkWin()


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
                checkWin()
                
    
    print s.encode()
    print "No more moves! The Game is a TIE!"
        
        
        
    
def HvC():
    print "Human Versus Computer!"
    
    ai = ""
    while (ai.strip() != '1' and ai.strip() != '2'):
        ai = raw_input("Computer is minimix or with alpha-beta? (enter 1 or 2): ")
    
    if ai == "2":
        nc = ComputerPlus(s) #for HvC
    else:
        nc = Computer(s)
    
    #get player order
    player_order = ""
    while (player_order.strip() != '1' and player_order.strip() != '2'):
        player_order = raw_input("go first or second? (enter 1 or 2): ")
                
    while s.legal_moves() != "":

        if player_order == "1":
            print s.encode()
            getPlayerMove()
            print s.encode()
            getComputerMove(nc, "O")
        else:
            print s.encode()
            getComputerMove(nc, "X")
            print s.encode()
            getPlayerMove()                
    
    print s.encode()
    print "No more moves! The Game is a TIE!"
    
    
def CvC():
    print "Computer Versus Computer!"
            
    print s.encode()
    while s.legal_moves() != "":
        
        s.do_move(c.minimaxi(s, "X"))
        checkWin()
        print s.encode()
        print "Computer one moved"
        s.do_move(c.minimaxi(s, "O"))
        checkWin()
        print s.encode()
        print "Computer two moved"
        
    print "No more moves! The Game is a TIE!"

    
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
    

    
    
    
